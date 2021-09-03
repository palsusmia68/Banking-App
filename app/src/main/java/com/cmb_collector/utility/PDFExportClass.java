package com.cmb_collector.utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFExportClass {
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final int cacheSize = maxMemory / 8;
    private static LruCache<String, Bitmap> bitmapLruCache = new LruCache<>(cacheSize);

    public static File exportToPdfNormalView(View view, int maxWidth, int maxHeight, String folderName, String fileName, Rectangle pageSize) {
        File dir, file = null;

        Bitmap bitmap = getViewBitmap(view, maxWidth, maxHeight);

        if (bitmap != null) {

            Document document = new Document(pageSize);

            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir + File.separator + fileName + ".pdf");
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }

            document.open();
            Image image = bitmapToImage(bitmap, document);

            try {
                document.add(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            if (document.isOpen()) {
                document.close();
            }
        }

        return file;

    }

    public static File exportToPdfWithRecyclerView(View headerView, int maxWidth, int maxHeight, RecyclerView recyclerView,
                                                   String folderName, String fileName, Rectangle pageSize) {
        Bitmap rvBitmap = null;
        Image image = null;
        File file = null;
        Document document = new Document(pageSize);
        Bitmap bitmap = getViewBitmap(headerView, maxWidth, maxHeight);

        if (bitmap != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            if (adapter != null) {
                int size = adapter.getItemCount();
                int height = 0;

                for (int i = 0; i < size; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(headerView.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();

                    height += holder.itemView.getMeasuredHeight();
                }

                rvBitmap = Bitmap.createBitmap(headerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(rvBitmap);
                bigCanvas.drawColor(Color.WHITE);

                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file = new File(dir + File.separator + fileName + ".pdf");
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }

                //For Header
                try {
                    if (!document.isOpen()) {
                        document.open();
                    }
                    image = bitmapToImage(bitmap, document);
                    document.add(image);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }


                //For RecyclerView Content
                for (int i = 0; i < size; i++) {
                    try {
                        Bitmap bmp = bitmapLruCache.get(String.valueOf(i));
                        image = bitmapToImage(bmp, document);
                        if (!document.isOpen()) {
                            document.open();
                        }
                        document.add(image);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (document.isOpen()) {
                    document.close();
                }

            }
        }
        return file;
    }

    public static void addRVItemToView(int position, View rv_item, int maxRecyclerViewItemWidth, int maxRecyclerViewItemHeight) {
        Bitmap bitmapRVItem = getViewBitmap(rv_item, maxRecyclerViewItemWidth, maxRecyclerViewItemHeight);
        bitmapLruCache.put(String.valueOf(position), bitmapRVItem);
    }

    private static Bitmap getViewBitmap(View v, int maxWidth, int maxHeight) {
        ViewGroup.LayoutParams vParams = v.getLayoutParams();

        //If the View hasn't been attached to a layout, or had LayoutParams set
        //return null, or handle this case however you want
        if (vParams == null) {
            return null;
        }

        int wSpec = measureSpecFromDimension(vParams.width, maxWidth);
        int hSpec = measureSpecFromDimension(vParams.height, maxHeight);

        v.measure(wSpec, hSpec);

        final int width = v.getMeasuredWidth();
        final int height = v.getMeasuredHeight();

        //Cannot make a zero-width or zero-height bitmap
        if (width == 0 || height == 0) {
            return null;
        }

        v.layout(0, 0, width, height);

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        v.draw(canvas);

        return result;
    }

    private static int measureSpecFromDimension(int dimension, int maxDimension) {
        switch (dimension) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                return View.MeasureSpec.makeMeasureSpec(maxDimension, View.MeasureSpec.EXACTLY);
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                return View.MeasureSpec.makeMeasureSpec(maxDimension, View.MeasureSpec.AT_MOST);
            default:
                return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY);
        }
    }

    private static Image bitmapToImage(Bitmap bitmap, Document document) {
        Image image = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = Image.getInstance(stream.toByteArray());
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
