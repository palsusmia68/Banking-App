package com.cmb_collector.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "CMB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_RELATION = "relation";
    public static final String TABLEID_CMBRELATIONID = "relationid";
    public static final String TABLE_CMBRELATIONLIST = "relationlist";
    ///////////////////////////////////////////////////////////////////////////////////////
    public static final String TABLE_ACCOUNTNUMBER = "accountnumber";
    public static final String TABLE_ACCOUNTNUMBERID = "accountnumberid";
    public static final String TABLE_accountnumberlist = "accountnumberlist";
    public static final String TABLE_BALENCE = "accbalance";
    ///////////////////////////////////////////////////////
    public static final String TABLE_AGENTREGAMOUNTLIST = "agentregamountlist";
    public static final String TABLE_AGENTREGAMOUNTID = "agentregamountlistid";
    public static final String TABLE_REGVALUE = "regvalue";
    ///////////////////////////////////////////////////////////////////////////////////
    public static final String TABLE_INSERTMEMBER = "insertmember";
    public static final String TABLE_INSERTMEMBERID = "insertmemberid";
    public static final String TABLE_BCODE = "bcode";
    public static final String TABLE_FROMNO = "formno";
    public static final String TABLE_MEMBERNO = "memberno";
    public static final String TABLE_FPREFIX = "fprefix";
    public static final String TABLE_FATHER = "father";
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_PIN = "pin";
    public static final String TABLE_MEMBERDOB = "memberdob";
    public static final String TABLE_AGE = "age";
    public static final String TABLE_NOMINEE = "nominee";
    public static final String TABLE_NAGE = "nage";
    public static final String TABLE_MRELATION = "nrelation";
    public static final String TABLE_SEX = "sex";
    public static final String TABLE_PHONE = "phone";
    public static final String TABLE_EMAIL = "email";
    public static final String TABLE_PAN = "pan";
    public static final String TABLE_ADDHER = "adherno";

    public static final String TABLE_ACCNO = "accno";
    public static final String TABLE_BANKNAME = "bankname";
    public static final String TABLE_IFSC = "ifsc";
    public static final String TABLE_IDPROOF = "idproof";
    public static final String TABLE_IDPROOFDOCNO = "idproofdocno";
    public static final String TABLE_SPONSERCODE = "sponsercode";
    public static final String TABLE_DATEOFJOING = "dateofjoining";
    public static final String TABLE_REGEMT = "regemt";
    public static final String TABLE_SHAREAMOUNT = "shareamount";
    public static final String TABLE_MOOFSHARE = "noofshare";
    public static final String TABLE_PAYTYPE = "paytype";
    public static final String TABLE_USERID = "userid";
    public static final String TABLE_SBACCOUNT = "sbaccount";
    public static final String TABLE_STATUSINSERTMEMBER = "statusinsermember";
    /////////////////////////////////////////////////////////
    public static final String TABLE_REGISTERAMOUNT = "registeramount";
    public static final String TABLE_REGISTERAMOUNTID = "registeramountid";
    public static final String TABLE_REGISTERAMOUNTBALANCE = "registeramountbalance";
    ////////////////////////////////////////////////////////////////////////////////
    public static final String TABLE_INSERTAGENT = "insertagent";
    public static final String TABLE_INSERTAGENTID = "insertagentid";
    public static final String TABLE_AGENT_BCODE = "agentbcode";
    public static final String TABLE_AGENT_MEMBERMO = "memberno";
    public static final String TABLE_AGENT_ANAME = "aname";
    public static final String TABLE_AGENT_GNAME = "gname";
    public static final String TABLE_AGENT_ADD = "addval";
    public static final String TABLE_AGENT_ADOJ = "adoj";
    public static final String TABLE_AGENT_IC= "ic";
    public static final String TABLE_AGENT_RANK = "rank";
    public static final String TABLE_AGENT_REGAMOUNT = "regamount";
    public static final String TABLE_AGENT_PAYMODE = "paymode";
    public static final String TABLE_AGENT_SBACCOUNT = "sbaccount";
    public static final String TABLE_AGENT_USERNAME = "username";
    public static final String TABLE_STATUSAGENT = "statusagent";
    /////////////////////////////RD ////////////////////////////////////////////////////////
    public static final String TABLE_RDDATATABLE = "rddatatable";
    public static final String TABLE_RDID = "rdid";
    public static final String TABLE_RDAPPLICATIONMANE = "rdapplicationname";
    public static final String TABLE_RDAGENTCODE = "rdagentcode";
    public static final String TABLE_RDBCODE = "rdbcode";
    public static final String TABLE_RDPOLICYDATE = "policydate";
    public static final String TABLE_PINCODE = "pincode";
    public static final String TABLE_RDPLANCODE = "plancode";
    public static final String TABLE_RDPTABLE = "Ptable";
    public static final String TABLE_RDTERM = "term";
    public static final String TABLE_RDMODE = "mode";
    public static final String TABLE_RDAMOUNT = "amount";
    public static final String TABLE_RDDEPOSITAMOUNT = "depositamount";
    public static final String TABLE_RDMATUREAMOUNT = "matureamount";
    public static final String TABLE_RDPAYMODE = "paymode";
    public static final String TABLE_RDUSERID = "userid";
    public static final String TABLE_RDMEMBERCODE = "membercode";
    public static final String TABLE_RDSBACCOUNT = "sbAccount";
    public static final String TBALE_RDSTATUS = "rdstatus";
    ///////////////////////Invesmemt spinner RD ////////////////////////////
    public static final String TABLE_INVESTMENTPLANCODE = "investmentplacecode";
    public static final String TABLE_INVESTMENTPLANCODEID = "investmentplacecodeid";
    public static final String TABLE_INVESTMENTPLANCODECODE = "investmentplacecodecode";
    public static final String TABLE_INVESTMENTPLANCODERD = "investmentplacecoderd";
    public static final String TABLE_INVESTMENTPLANCODdRD = "investmentplacecodedrd";
    ///////////////////////Invesmemt spinner FD ////////////////////////////
    public static final String TABLE_INVESTMENTPLANCODEFD = "investmentplacecodefd";
    public static final String TABLE_INVESTMENTPLANCODEFDID = "investmentplacecodefdid";
    public static final String TABLE_INVESTMENTPLANCODEE = "investmentplacecodee";
    public static final String TABLE_INVESTMENTPLANCODEFDD = "investmentplacecodefdd";
    ////////////////////////////////////////////////MS //////////////////////////////////////
    public static final String TABLE_INVESTMENTPLANCODEMISTABLE = "investmentplacecodemistable";
    public static final String TABLE_INVESTMENTPLANCODEMISID = "investmentplacecodemisid";
    public static final String TABLE_INVESTMENTPLANCODEMISCODE = "investmentplacecodemiscode";
    public static final String TABLE_INVESTMENTPLANCODEMIS = "investmentplacecodemis";
   /////////////////////////////////////////////plancode table rd/////////////////////////////////////////////
//    public static final String TABLE_PLANCODETABLERDUPDATE = "plancodetablerdupdate";
//    public static final String TABLE_PLANCODETABLERDIDUPDATE = "plancodetablerdidupdate";
//    public static final String TABLE_PLANCODERD = "plancoderd";
//    public static final String TABLE_PLANSTABLE = "planstable";
//    public static final String TABLE_PLANMINAMOUNT = "planminamount";
//    public static final String TABLE_PLANMODEFLAG = "planmodeflag";
   public static final String TABLE_PLANTABLE = "plantable";
    public static final String TABLE_PLANTABLEID = "plantableid";
    public static final String TABLE_PLANSCHEME = "plancoderd";
    public static final String TABLE_PLANSTABLE = "planstable";
    public static final String TABLE_PLANMINAMOUNT = "planminamount";
    public static final String TABLE_PLANMODEFLAG = "planmodeflag";
    /////////////////////////////////////////////plancode table fd/////////////////////////////////////////////
    public static final String TABLE_PLANCODETABLEFD = "plancodetable";
    public static final String TABLE_PLANCODETABLEFDIDD = "plancodetablefdidd";
    public static final String TABLE_PLANCODEFD = "plancodefd";
    public static final String TABLE_PLNCODEFD = "plancodef";
    /////////////////////////////////////////////plancode table mis/////////////////////////////////////////////
    public static final String TABLE_PLANCODETABLEMIS = "plancodetablemis";
    public static final String TABLE_PLANCODMIS = "plancodemisdid";
    public static final String TABLE_PLANCODEMIS = "plancodemis";
    public static final String TABLE_PLNCODEMIS = "planmis";

    ///////////////////////////////New Account ///////////////////////////////
    public static final String TABLE_NEWACDATATABLE = "newacdatatable";
    public static final String TABLE_NEWACID = "newacid";
    public static final String TABLE_NEWACBRANCH = "newacbranch";
    public static final String TABLE_NEWACDATEOFENT = "newacdateofent";
    public static final String TABLE_NEWACMEMBERCODE = "newacmembercode";
    public static final String TABLE_NEWACAPPLICANTNAME = "newacapplicantname";
    public static final String TABLE_NEWACFATHER = "newacfather";
    public static final String TABLE_NEWACADDRESS = "newacaddress";
    public static final String TABLE_NEWACPHONENO = "newacphoneno";
    public static final String TABLE_NEWACACCOUNTTYPE = "newacaccounttype";
    public static final String TABLE_NEWACJOINTAPPLICANTNAME = "newacjointapplicantname";
    public static final String TABLE_NEWACJOINTFATHER = "newacjointfather";
    public static final String TABLE_NEWACJOINTADDRESS = "newacjointaddress";
    public static final String TABLE_NEWACADVISORCODE = "newacadvisorcode";
    public static final String TABLE_NEWACAMOUNT = "newacamount";
    public static final String TABLE_NEWACPAYMODE = "newacpaymode";
    public static final String TABLE_NEWACUSERID = "newacuserid";
    public static final String TABLE_NEWACSBACCOUNT = "newacsbaccount";
    public static final String TABLE_NEWACSTATUS = "newacstatus";
    ///////////////////////////////Account Transaction////////////////////
    public static final String TABLE_ACCOUNTTRANSACTION = "accounttransaction";
    public static final String TABLE_ACCOUNTTRANSACTIONID = "accounttransactionId";
    public static final String TABLE_ATBRANCH = "atbranch";
    public static final String TABLE_ATSBACCOUNT = "atsbaccount";
    public static final String TABLE_ATACCOUNTNO = "ataccountno";
    public static final String TABLE_ATPAYEENAME = "atpayeename";
    public static final String TABLE_ATTRANTYPE = "attrantype";
    public static final String TABLE_ATAMOUNT = "atamount";
    public static final String TABLE_ATTDATE = "attdate";
    public static final String TABLE_ATUSERID = "atuserid";
    public static final String TABLE_ATREMARKS = "atremarks";
    public static final String TABLE_ATSTATUS = "atstatus";
    ///////////////////////////New Loan/////////////////////////////////////////
    public static final String TABLE_NEWLOANTABLE = "newloantable";
    public static final String TABLE_NEWLOANID = "newloanid";
    public static final String TABLE_NEWLOANBRANCH = "newloanbranch";
    public static final String TABLE_NEWLOANMEMBERCODE = "newloanmembercode";
    public static final String TABLE_NEWLOANMEMBERNAME = "newloanmembername";
    public static final String TABLE_NEWLOANFATHERNAME = "newloanfathername";
    public static final String TABLE_NEWLOANADDRESS = "newloanaddress";
    public static final String TABLE_NEWLOANREGDATE = "newloanregdate";
    public static final String TABLE_NEWLOANSCHEME = "newloanscheme";
    public static final String TABLE_NEWLOANTERM = "newloanterm";
    public static final String TABLE_NEWLOANMODE = "newloanmode";
    public static final String TABLE_NEWLOANROI = "newloanroi";
    public static final String TABLE_NEWLOANINTERESTMODE = "newloaninterestmode";
    public static final String TABLE_NEWLOANLOANAMOUNT = "newloanamount";
    public static final String TABLE_NEWLOANEMIAMOUNT = "newloanemiamount";
    public static final String TABLE_NEWLOANCOLLECTORCODE = "newloancollectorcode";
    public static final String TABLE_NEWLOANPURPOSE = "newloanpurpose";
    public static final String TABLE_NEWLOANUSERID = "newloanuserid";
    public static final String TABLE_NEWLOANSTATUS = "newloanstatus";
    //////////////////////////////////RD-12///////////////////////////////////////
    public static final String TABLE_RD12TABLE = "rdtwelvetable";
    public static final String TABLE_RD12TABLEID = "rdtwelvetableid";
    public static final String TABLE_MODE = "mode";
    public static final String TABLE_TERM = "term";
    public static final String TABLE_ROI = "roi";
    /////////////////////////////////////FD-12////////////////////////////////////////
    public static final String TABLE_FD12TABLE = "fdtwelvetable";
    public static final String TABLE_FD12TABLEID = "fdtwelvetableid";
    public static final String TABLE_MODEFD = "modefd";
    public static final String TABLE_TERMFD = "termfd";
    public static final String TABLE_ROIFD = "roifd";
    //////////////////////////////////////////////////////////MIS60//////////////////////////////////
    public static final String TABLE_MIS60TABLE = "mistwelvetable";
    public static final String TABLE_MIS60TABLEID = "mistwelvetableid";
    public static final String TABLE_MODEFDMIS = "modemis";
    public static final String TABLE_TERMMIS = "termmis";
    public static final String TABLE_ROIMIS = "roimis";
    ///////////////////////////////////////////////////////////////////////////////
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE " + TABLE_RELATION
                + "(" + TABLEID_CMBRELATIONID +
                " INTEGER PRIMARY KEY, "
                + TABLE_CMBRELATIONLIST + " VARCHAR);";
        db.execSQL(sql1);
        String sql2 = "CREATE TABLE " + TABLE_ACCOUNTNUMBER
                + "(" + TABLE_ACCOUNTNUMBERID +
                " INTEGER PRIMARY KEY, "
                + TABLE_accountnumberlist + " VARCHAR,"
                + TABLE_BALENCE + " VARCHAR);";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE " + TABLE_INSERTMEMBER
                + "(" + TABLE_INSERTMEMBERID +
                " INTEGER PRIMARY KEY, "
                + TABLE_BCODE + " VARCHAR,"
                + TABLE_FROMNO + " VARCHAR,"
                + TABLE_MEMBERNO + " VARCHAR,"
                + TABLE_FPREFIX + " VARCHAR,"
                + TABLE_FATHER + " VARCHAR,"
                + TABLE_ADDRESS + " VARCHAR,"
                + TABLE_PIN + " VARCHAR,"
                + TABLE_MEMBERDOB + " VARCHAR,"
                + TABLE_AGE + " VARCHAR,"
                + TABLE_NOMINEE + " VARCHAR,"
                + TABLE_NAGE + " VARCHAR,"
                + TABLE_MRELATION + " VARCHAR,"
                + TABLE_SEX + " VARCHAR,"
                + TABLE_PHONE + " VARCHAR,"
                + TABLE_EMAIL + " VARCHAR,"
                + TABLE_PAN + " VARCHAR,"
                + TABLE_ADDHER + " VARCHAR,"
                + TABLE_ACCNO + " VARCHAR,"
                + TABLE_BANKNAME + " VARCHAR,"
                + TABLE_IFSC + " VARCHAR,"
                + TABLE_IDPROOF + " VARCHAR,"
                + TABLE_IDPROOFDOCNO + " VARCHAR,"
                + TABLE_SPONSERCODE + " VARCHAR,"
                + TABLE_DATEOFJOING + " VARCHAR,"
                + TABLE_REGEMT + " VARCHAR,"
                + TABLE_SHAREAMOUNT + " VARCHAR,"
                + TABLE_MOOFSHARE + " VARCHAR,"
                + TABLE_PAYTYPE + " VARCHAR,"
                + TABLE_USERID + " VARCHAR,"
                + TABLE_SBACCOUNT + " VARCHAR,"
                + TABLE_STATUSINSERTMEMBER + " TINYINT);";
        db.execSQL(sql3);
        String sql4 = "CREATE TABLE " + TABLE_REGISTERAMOUNT
                + "(" + TABLE_REGISTERAMOUNTID +
                " INTEGER PRIMARY KEY, "
                + TABLE_REGISTERAMOUNTBALANCE + " VARCHAR);";
        db.execSQL(sql4);

        String sql5 = "CREATE TABLE " + TABLE_INSERTAGENT
                + "(" + TABLE_INSERTAGENTID +
                " INTEGER PRIMARY KEY, "
                + TABLE_AGENT_BCODE + " VARCHAR,"
                + TABLE_AGENT_MEMBERMO + " VARCHAR,"
                + TABLE_AGENT_ANAME + " VARCHAR,"
                + TABLE_AGENT_GNAME + " VARCHAR,"
                + TABLE_AGENT_ADD + " VARCHAR,"
                + TABLE_AGENT_ADOJ + " VARCHAR,"
                + TABLE_AGENT_IC + " VARCHAR,"
                + TABLE_AGENT_RANK + " VARCHAR,"
                + TABLE_AGENT_REGAMOUNT + " VARCHAR,"
                + TABLE_AGENT_PAYMODE + " VARCHAR,"
                + TABLE_AGENT_SBACCOUNT + " VARCHAR,"
                + TABLE_AGENT_USERNAME + " VARCHAR,"
                + TABLE_STATUSAGENT + " TINYINT);";
        db.execSQL(sql5);

        String sql6 = "CREATE TABLE " + TABLE_AGENTREGAMOUNTLIST
                + "(" + TABLE_AGENTREGAMOUNTID +
                " INTEGER PRIMARY KEY, "
                + TABLE_REGVALUE + " VARCHAR);";
        db.execSQL(sql6);

        String sql7 = "CREATE TABLE " + TABLE_RDDATATABLE
                + "(" + TABLE_RDID +
                " INTEGER PRIMARY KEY, "
                + TABLE_RDAPPLICATIONMANE + " VARCHAR,"
                + TABLE_RDAGENTCODE + " VARCHAR,"
                + TABLE_AGENT_MEMBERMO + " VARCHAR,"
                + TABLE_RDBCODE + " VARCHAR,"
                + TABLE_RDPOLICYDATE + " VARCHAR,"
                + TABLE_PINCODE + " VARCHAR,"
                + TABLE_RDPLANCODE + " VARCHAR,"
                + TABLE_RDPTABLE + " VARCHAR,"
                + TABLE_RDTERM + " VARCHAR,"
                + TABLE_RDMODE + " VARCHAR,"
                + TABLE_RDAMOUNT + " VARCHAR,"
                + TABLE_RDDEPOSITAMOUNT + " VARCHAR,"
                + TABLE_RDMATUREAMOUNT + " VARCHAR,"
                + TABLE_RDPAYMODE + " VARCHAR,"
                + TABLE_RDUSERID + " VARCHAR,"
                + TABLE_RDMEMBERCODE + " VARCHAR,"
                + TABLE_RDSBACCOUNT + " VARCHAR,"
                + TBALE_RDSTATUS + " TINYINT);";
        db.execSQL(sql7);

        String sql8 = "CREATE TABLE " + TABLE_INVESTMENTPLANCODE
                + "(" + TABLE_INVESTMENTPLANCODEID +
                " INTEGER PRIMARY KEY, "
                + TABLE_INVESTMENTPLANCODECODE + " VARCHAR,"
                + TABLE_INVESTMENTPLANCODERD + " VARCHAR,"
                + TABLE_INVESTMENTPLANCODdRD + " VARCHAR);";
        db.execSQL(sql8);
        String sql9 = "CREATE TABLE " + TABLE_INVESTMENTPLANCODEFD
                + "(" + TABLE_INVESTMENTPLANCODEFDID +
                " INTEGER PRIMARY KEY, "
                + TABLE_INVESTMENTPLANCODEE + " VARCHAR,"
                + TABLE_INVESTMENTPLANCODEFDD + " VARCHAR);";
        db.execSQL(sql9);
        String sql10 = "CREATE TABLE " + TABLE_INVESTMENTPLANCODEMISTABLE
                + "(" + TABLE_INVESTMENTPLANCODEMISID +
                " INTEGER PRIMARY KEY, "
                + TABLE_INVESTMENTPLANCODEMISCODE + " VARCHAR,"
                + TABLE_INVESTMENTPLANCODEMIS + " VARCHAR);";
        db.execSQL(sql10);

        String sql11 = "CREATE TABLE " + TABLE_PLANTABLE
                + "(" + TABLE_PLANTABLEID +
                " INTEGER PRIMARY KEY, "
                + TABLE_PLANSCHEME + "VARCHAR,"
                + TABLE_PLANSTABLE + " VARCHAR,"
                + TABLE_PLANMINAMOUNT + "VARCHAR,"
                + TABLE_PLANMODEFLAG + " VARCHAR);";
        db.execSQL(sql11);
//        String sql20 = "CREATE TABLE " + TABLE_PLANCODETABLERDUPDATE
//                + "(" + TABLE_PLANCODETABLERDIDUPDATE +
//                " INTEGER PRIMARY KEY, "
//                + TABLE_PLANCODERD + "VARCHAR,"
//                + TABLE_PLANSTABLE + " VARCHAR,"
//                + TABLE_PLANMODEFLAG + "VARCHAR,"
//                + TABLE_PLANMINAMOUNT + " VARCHAR);";
//        db.execSQL(sql20);
        String sql12 = "CREATE TABLE " + TABLE_PLANCODETABLEFD
                + "(" + TABLE_PLANCODETABLEFDIDD +
                " INTEGER PRIMARY KEY, "
                + TABLE_PLANCODEFD + " VARCHAR,"
                + TABLE_PLNCODEFD + " VARCHAR);";
        db.execSQL(sql12);

        String sql13 = "CREATE TABLE " + TABLE_PLANCODETABLEMIS
                + "(" + TABLE_PLANCODMIS +
                " INTEGER PRIMARY KEY, "
                + TABLE_PLANCODEMIS + " VARCHAR,"
                + TABLE_PLNCODEMIS + " VARCHAR);";
        db.execSQL(sql13);

        String sql14 = "CREATE TABLE " + TABLE_NEWACDATATABLE
                + "(" + TABLE_NEWACID +
                " INTEGER PRIMARY KEY, "
                + TABLE_NEWACBRANCH + " VARCHAR,"
                + TABLE_NEWACDATEOFENT + " VARCHAR,"
                + TABLE_NEWACMEMBERCODE + " VARCHAR,"
                + TABLE_NEWACAPPLICANTNAME + " VARCHAR,"
                + TABLE_NEWACFATHER + " VARCHAR,"
                + TABLE_NEWACADDRESS + " VARCHAR,"
                + TABLE_NEWACPHONENO + " VARCHAR,"
                + TABLE_NEWACACCOUNTTYPE + " VARCHAR,"
                + TABLE_NEWACJOINTAPPLICANTNAME + " VARCHAR,"
                + TABLE_NEWACJOINTFATHER + " VARCHAR,"
                + TABLE_NEWACJOINTADDRESS + " VARCHAR,"
                + TABLE_NEWACADVISORCODE + " VARCHAR,"
                + TABLE_NEWACAMOUNT + " VARCHAR,"
                + TABLE_NEWACPAYMODE + " VARCHAR,"
                + TABLE_NEWACUSERID + " VARCHAR,"
                + TABLE_NEWACSBACCOUNT + " VARCHAR,"
                + TABLE_NEWACSTATUS + " TINYINT);";
        db.execSQL(sql14);
        String sql15 = "CREATE TABLE " + TABLE_ACCOUNTTRANSACTION
                + "(" + TABLE_ACCOUNTTRANSACTIONID +
                " INTEGER PRIMARY KEY, "
                + TABLE_ATBRANCH + " VARCHAR,"
                + TABLE_ATSBACCOUNT + " VARCHAR,"
                + TABLE_ATACCOUNTNO + " VARCHAR,"
                + TABLE_ATPAYEENAME + " VARCHAR,"
                + TABLE_ATTRANTYPE + " VARCHAR,"
                + TABLE_ATAMOUNT + " VARCHAR,"
                + TABLE_ATTDATE + " VARCHAR,"
                + TABLE_ATUSERID + " VARCHAR,"
                + TABLE_ATREMARKS + " VARCHAR,"
                + TABLE_ATSTATUS + " TINYINT);";
        db.execSQL(sql15);
        String sql16 = "CREATE TABLE " + TABLE_NEWLOANTABLE
                + "(" + TABLE_NEWLOANID +
                " INTEGER PRIMARY KEY, "
                + TABLE_NEWLOANBRANCH + " VARCHAR,"
                + TABLE_NEWLOANMEMBERCODE + " VARCHAR,"
                + TABLE_NEWLOANMEMBERNAME + " VARCHAR,"
                + TABLE_NEWLOANFATHERNAME + " VARCHAR,"
                + TABLE_NEWLOANADDRESS + " VARCHAR,"
                + TABLE_NEWLOANREGDATE + " VARCHAR,"
                + TABLE_NEWLOANSCHEME + " VARCHAR,"
                + TABLE_NEWLOANTERM + " VARCHAR,"
                + TABLE_NEWLOANMODE + " VARCHAR,"
                + TABLE_NEWLOANROI + " VARCHAR,"
                + TABLE_NEWLOANINTERESTMODE + " VARCHAR,"
                + TABLE_NEWLOANLOANAMOUNT + " VARCHAR,"
                + TABLE_NEWLOANEMIAMOUNT + " VARCHAR,"
                + TABLE_NEWLOANCOLLECTORCODE + " VARCHAR,"
                + TABLE_NEWLOANPURPOSE + " VARCHAR,"
                + TABLE_NEWLOANUSERID + " VARCHAR,"
                + TABLE_NEWLOANSTATUS + " TINYINT);";
        db.execSQL(sql16);
        String sql17 = "CREATE TABLE " + TABLE_RD12TABLE
                + "(" + TABLE_RD12TABLEID +
                " INTEGER PRIMARY KEY, "
                + TABLE_MODE + " VARCHAR,"
                + TABLE_TERM + " VARCHAR,"
                + TABLE_ROI + " VARCHAR);";
        db.execSQL(sql17);
        String sql18 = "CREATE TABLE " + TABLE_FD12TABLE
                + "(" + TABLE_FD12TABLEID +
                " INTEGER PRIMARY KEY, "
                + TABLE_MODEFD + " VARCHAR,"
                + TABLE_TERMFD + " VARCHAR,"
                + TABLE_ROIFD + " VARCHAR);";
        db.execSQL(sql18);
        String sql19 = "CREATE TABLE " + TABLE_MIS60TABLE
                + "(" + TABLE_MIS60TABLEID +
                " INTEGER PRIMARY KEY, "
                + TABLE_MODEFDMIS + " VARCHAR,"
                + TABLE_TERMMIS + " VARCHAR,"
                + TABLE_ROIMIS + " VARCHAR);";
        db.execSQL(sql19);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql1 = "DROP TABLE IF EXISTS TABLE_RELATION";
        db.execSQL(sql1);
        String sql2 = "DROP TABLE IF EXISTS TABLE_ACCOUNTNUMBER";
        db.execSQL(sql2);
        String sql3 = "DROP TABLE IF EXISTS TABLE_INSERTMEMBER";
        db.execSQL(sql3);
        String sql4 = "DROP TABLE IF EXISTS TABLE_REGISTERAMOUNT";
        db.execSQL(sql4);
        String sql5 = "DROP TABLE IF EXISTS TABLE_INSERTAGENT";
        db.execSQL(sql5);
        String sql6 = "DROP TABLE IF EXISTS TABLE_AGENTREGAMOUNTLIST";
        db.execSQL(sql6);
        String sql7 = "DROP TABLE IF EXISTS TABLE_RDDATATABLE";
        db.execSQL(sql7);
        String sql8 = "DROP TABLE IF EXISTS TABLE_INVESTMENTPLANCODE";
        db.execSQL(sql8);
        String sql9 = "DROP TABLE IF EXISTS TABLE_INVESTMENTPLANCODEFD";
        db.execSQL(sql9);
        String sql10 = "DROP TABLE IF EXISTS TABLE_INVESTMENTPLANCODEMISTABLE";
        db.execSQL(sql10);
        String sql11 = "DROP TABLE IF EXISTS TABLE_PLANTABLE";
        db.execSQL(sql11);
        String sql12 = "DROP TABLE IF EXISTS TABLE_PLANCODETABLEFD";
        db.execSQL(sql12);
        String sql13 = "DROP TABLE IF EXISTS TABLE_PLANCODETABLEMIS";
        db.execSQL(sql13);
        String sql14 = "DROP TABLE IF EXISTS TABLE_NEWACDATATABLE";
        db.execSQL(sql14);
        String sql15 = "DROP TABLE IF EXISTS TABLE_ACCOUNTTRANSACTION";
        db.execSQL(sql15);
        String sql16 = "DROP TABLE IF EXISTS TABLE_NEWLOANTABLE";
        db.execSQL(sql16);
        String sql17 = "DROP TABLE IF EXISTS TABLE_RD12TABLE";
        db.execSQL(sql17);
        String sql18 = "DROP TABLE IF EXISTS TABLE_FD12TABLE";
        db.execSQL(sql18);
        String sql19 = "DROP TABLE IF EXISTS TABLE_MIS60TABLE";
        db.execSQL(sql19);
    }

    public boolean relationInsert(String relationcmblist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_CMBRELATIONLIST, relationcmblist);
        db.insert(TABLE_RELATION, null, contentValues);
        db.close();
        return true;
    }
    public boolean InsertRD(String relationcmblist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_CMBRELATIONLIST, relationcmblist);
        db.insert(TABLE_RELATION, null, contentValues);
        db.close();
        return true;
    }
    public boolean relationUpdate(String relationidd, String relationcmblist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLEID_CMBRELATIONID, relationidd);
        contentValues.put(TABLE_CMBRELATIONLIST, relationcmblist);
        db.update(TABLE_RELATION, contentValues, "relationid = ?", new String[]{relationidd});
        db.close();
        return true;
    }

    public Cursor getRelationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_RELATION, null);
        return res;
    }

    public boolean accountInsert(String AccountNo,String accountbal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_accountnumberlist, AccountNo);
        contentValues.put(TABLE_BALENCE, accountbal);
        db.insert(TABLE_ACCOUNTNUMBER, null, contentValues);
        db.close();
        return true;
    }

    public boolean accountUpdate(String accountnumberidd,String accountlist,String accountbal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ACCOUNTNUMBERID, accountnumberidd);
        contentValues.put(TABLE_accountnumberlist, accountlist);
        contentValues.put(TABLE_BALENCE, accountbal);
        db.update(TABLE_ACCOUNTNUMBER, contentValues, "accountnumberid = ?",new String[] { accountnumberidd });
        db.close();
        return true;
    }

    public Cursor getAccountnumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ACCOUNTNUMBER, null);
        return res;
    }
    //////////////////////////////////////////////////////////////
    public boolean memberInsert(String bcode,String formno,String memberno,
                                String fprefix,String father,
                                String address,String pin,
                                String memberdob,
                                String age,String nominee,
                                String nage,String nrelation,String sex,String phone,
                                String email,String pan,String adherno,String accno,String bankname,
                                String ifsc,String idproof,String idproofdocno,
                                String sponsercode,String dateofjoining,String regemt,
                                String shareamount,String noofshare,String paytype,String userid,String sbaccount,int statusinsermember) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_BCODE, bcode);
        contentValues.put(TABLE_FROMNO, formno);
        contentValues.put(TABLE_MEMBERNO, memberno);
        contentValues.put(TABLE_FPREFIX, fprefix);
        contentValues.put(TABLE_FATHER, father);
        contentValues.put(TABLE_ADDRESS, address);
        contentValues.put(TABLE_PIN, pin);
        contentValues.put(TABLE_MEMBERDOB, memberdob);
        contentValues.put(TABLE_AGE, age);
        contentValues.put(TABLE_NOMINEE, nominee);
        contentValues.put(TABLE_NAGE, nage);
        contentValues.put(TABLE_MRELATION, nrelation);
        contentValues.put(TABLE_SEX, sex);
        contentValues.put(TABLE_PHONE, phone);
        contentValues.put(TABLE_EMAIL, email);
        contentValues.put(TABLE_PAN, pan);
        contentValues.put(TABLE_ADDHER, adherno);
        contentValues.put(TABLE_ACCNO, accno);
        contentValues.put(TABLE_BANKNAME, bankname);
        contentValues.put(TABLE_IFSC, ifsc);
        contentValues.put(TABLE_IDPROOF, idproof);
        contentValues.put(TABLE_IDPROOFDOCNO, idproofdocno);
        contentValues.put(TABLE_SPONSERCODE, sponsercode);
        contentValues.put(TABLE_DATEOFJOING, dateofjoining);
        contentValues.put(TABLE_REGEMT, regemt);
        contentValues.put(TABLE_SHAREAMOUNT, shareamount);
        contentValues.put(TABLE_MOOFSHARE, noofshare);
        contentValues.put(TABLE_PAYTYPE, paytype);
        contentValues.put(TABLE_USERID, userid);
        contentValues.put(TABLE_SBACCOUNT, sbaccount);
        contentValues.put(TABLE_STATUSINSERTMEMBER,statusinsermember);
        db.insert(TABLE_INSERTMEMBER, null, contentValues);
        db.close();
        return true;
    }
    public boolean memberUpdate(String insertmemberidd,String bcode,String formno,String memberno,
                                String fprefix,String father,
                                String address,String pin,
                                String memberdob,
                                String age,String nominee,
                                String nage,String nrelation,String sex,String phone,
                                String email,String pan,String adherno,String accno,String bankname,
                                String ifsc,String idproof,String idproofdocno,
                                String sponsercode,String dateofjoining,String regemt,
                                String shareamount,String noofshare,String paytype,String userid,String sbaccount,int statusinsermember) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_INSERTMEMBERID, insertmemberidd);
        contentValues.put(TABLE_BCODE, bcode);
        contentValues.put(TABLE_FROMNO, formno);
        contentValues.put(TABLE_MEMBERNO, memberno);
        contentValues.put(TABLE_FPREFIX, fprefix);
        contentValues.put(TABLE_FATHER, father);
        contentValues.put(TABLE_ADDRESS, address);
        contentValues.put(TABLE_PIN, pin);
        contentValues.put(TABLE_MEMBERDOB, memberdob);
        contentValues.put(TABLE_AGE, age);
        contentValues.put(TABLE_NOMINEE, nominee);
        contentValues.put(TABLE_NAGE, nage);
        contentValues.put(TABLE_MRELATION, nrelation);
        contentValues.put(TABLE_SEX, sex);
        contentValues.put(TABLE_PHONE, phone);
        contentValues.put(TABLE_EMAIL, email);
        contentValues.put(TABLE_PAN, pan);
        contentValues.put(TABLE_ADDHER, adherno);
        contentValues.put(TABLE_ACCNO, accno);
        contentValues.put(TABLE_BANKNAME, bankname);
        contentValues.put(TABLE_IFSC, ifsc);
        contentValues.put(TABLE_IDPROOF, idproof);
        contentValues.put(TABLE_IDPROOFDOCNO, idproofdocno);
        contentValues.put(TABLE_SPONSERCODE, sponsercode);
        contentValues.put(TABLE_DATEOFJOING, dateofjoining);
        contentValues.put(TABLE_REGEMT, regemt);
        contentValues.put(TABLE_SHAREAMOUNT, shareamount);
        contentValues.put(TABLE_MOOFSHARE, noofshare);
        contentValues.put(TABLE_PAYTYPE, paytype);
        contentValues.put(TABLE_USERID, userid);
        contentValues.put(TABLE_SBACCOUNT, sbaccount);
        contentValues.put(TABLE_STATUSINSERTMEMBER,statusinsermember);
        db.update(TABLE_INSERTMEMBER, contentValues, "insertmemberid = ?",new String[] { insertmemberidd });
        db.close();
        return true;
    }
    public Cursor getInsertmembervalue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INSERTMEMBER, null);
        return res;
    }

    public boolean updateInsertmemberSyncStatus(int insertmemberid, int statusinsermember) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_STATUSINSERTMEMBER, statusinsermember);
        db.update(TABLE_INSERTMEMBER, contentValues, TABLE_INSERTMEMBERID + "=" + insertmemberid, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedInsert() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_INSERTMEMBER + " WHERE " + TABLE_STATUSINSERTMEMBER + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    ///////////////////////////////////////////////////////////////////////////////////
    public boolean RegisteramountInsert(String registeramountbalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_REGISTERAMOUNTBALANCE, registeramountbalance);
        db.insert(TABLE_REGISTERAMOUNT, null, contentValues);
        db.close();
        return true;
    }

    public boolean RegisteramountUpdate(String registeramountidd,String registeramountbalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_REGISTERAMOUNTID, registeramountidd);
        contentValues.put(TABLE_REGISTERAMOUNTBALANCE, registeramountbalance);
        db.update(TABLE_REGISTERAMOUNT, contentValues, "registeramountid = ?",new String[] { registeramountidd });
        db.close();
        return true;
    }

    public Cursor getregistervalue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_REGISTERAMOUNT, null);
        return res;
    }
    /////////////////////// insert agent ////////////////////////////
    public boolean agentInsert(String agentbcode,
                               String memberno,String aname,
                               String gname,String add,String adoj,String ic,String rank,
                               String regamount,String paymode,String sbaccount,String username,int statusagent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_AGENT_BCODE,agentbcode);
        contentValues.put(TABLE_AGENT_MEMBERMO, memberno);
        contentValues.put(TABLE_AGENT_ANAME, aname);
        contentValues.put(TABLE_AGENT_GNAME, gname);
        contentValues.put(TABLE_AGENT_ADD, add);
        contentValues.put(TABLE_AGENT_ADOJ,adoj);
        contentValues.put(TABLE_AGENT_IC,ic);
        contentValues.put(TABLE_AGENT_RANK,rank);
        contentValues.put(TABLE_AGENT_REGAMOUNT,regamount);
        contentValues.put(TABLE_AGENT_PAYMODE,paymode);
        contentValues.put(TABLE_AGENT_SBACCOUNT,sbaccount);
        contentValues.put(TABLE_AGENT_USERNAME,username);
        contentValues.put(TABLE_STATUSAGENT,statusagent);
        db.insert(TABLE_INSERTAGENT, null, contentValues);
        db.close();
        return true;
    }
    public boolean agentUpdate(String insertagentid,String agentbcode,
                               String memberno,String aname,
                               String gname,String add,String adoj,String ic,String rank,
                               String regamount,String paymode,String sbaccount,String username,String statusagent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_INSERTAGENTID, insertagentid);
        contentValues.put(TABLE_AGENT_BCODE,agentbcode);
        contentValues.put(TABLE_AGENT_MEMBERMO, memberno);
        contentValues.put(TABLE_AGENT_ANAME, aname);
        contentValues.put(TABLE_AGENT_GNAME, gname);
        contentValues.put(TABLE_AGENT_ADD, add);
        contentValues.put(TABLE_AGENT_ADOJ,adoj);
        contentValues.put(TABLE_AGENT_IC,ic);
        contentValues.put(TABLE_AGENT_RANK,rank);
        contentValues.put(TABLE_AGENT_REGAMOUNT,regamount);
        contentValues.put(TABLE_AGENT_PAYMODE,paymode);
        contentValues.put(TABLE_AGENT_SBACCOUNT,sbaccount);
        contentValues.put(TABLE_AGENT_USERNAME,username);
        contentValues.put(TABLE_STATUSAGENT,statusagent);
        db.update(TABLE_INSERTAGENT, contentValues, "insertagentid = ?",new String[] { insertagentid });
        db.close();
        return true;
    }
    public Cursor agentInsertmembervalue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INSERTAGENT, null);
        return res;
    }
    public boolean updateAGENTmemberSyncStatus(int insertmemberid, int statusagent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_STATUSAGENT, statusagent);
        db.update(TABLE_INSERTAGENT, contentValues, TABLE_INSERTAGENTID + "=" + insertmemberid, null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedInsertaGENT() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_INSERTAGENT + " WHERE " + TABLE_STATUSAGENT + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    ////////////////////////////////////////////////////////////////////////////////
    public boolean AgentRegAmountListInsert(String regvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_REGVALUE, regvalue);
        db.insert(TABLE_AGENTREGAMOUNTLIST, null, contentValues);
        db.close();
        return true;
    }

    public boolean AgentRegAmountListUPDATE(String agentregamountlistidd,String regvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_AGENTREGAMOUNTID, agentregamountlistidd);
        contentValues.put(TABLE_REGVALUE, regvalue);
        db.update(TABLE_AGENTREGAMOUNTLIST, contentValues, "agentregamountlistid = ?",new String[] { agentregamountlistidd });
        db.close();
        return true;
    }
    public Cursor AgentRegAmount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_AGENTREGAMOUNTLIST, null);
        return res;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    public boolean INSDRTFD(String investmentplacecodecode,String investmentplacecoderd,String investmentplacecodedrd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_INVESTMENTPLANCODECODE, investmentplacecodecode);
        contentValues.put(TABLE_INVESTMENTPLANCODERD, investmentplacecoderd);
        contentValues.put(TABLE_INVESTMENTPLANCODdRD, investmentplacecodedrd);
        db.insert(TABLE_INVESTMENTPLANCODE, null, contentValues);
        db.close();
        return true;
    }
    public Cursor getrd() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INVESTMENTPLANCODE, null);
        return res;
    }
    public boolean INSDRRD(String plancoderd,String planstable,String planminamount,String planmodeflag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_PLANSCHEME, plancoderd);
        contentValues.put(TABLE_PLANSTABLE, planstable);
        contentValues.put(TABLE_PLANMINAMOUNT, planminamount);
        contentValues.put(TABLE_PLANMODEFLAG, planmodeflag);
        db.insert(TABLE_PLANTABLE, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getfd() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INVESTMENTPLANCODEFD, null);
        return res;
    }
    public boolean INSDRMIS(String investmentplacecodemiscode,String investmentplacecodemis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_INVESTMENTPLANCODEMISCODE, investmentplacecodemiscode);
        contentValues.put(TABLE_INVESTMENTPLANCODEMIS, investmentplacecodemis);
        db.insert(TABLE_INVESTMENTPLANCODEMISTABLE, null, contentValues);
        db.close();
        return true;
    }
    public Cursor getMIS() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_INVESTMENTPLANCODEMISTABLE, null);
        return res;
    }

//    public boolean InsertRD(String plancoderd,String planstable, String planminamount, String planmodeflag) {
//        SQLiteDatabase db = this.getWritableDatabase();
//     //   System.out.println("insertRD"+"i am");
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TABLE_PLANCODERD, plancoderd);
//        contentValues.put(TABLE_PLANSTABLE, planstable);
//        contentValues.put(TABLE_PLANMINAMOUNT, planminamount);
//        contentValues.put(TABLE_PLANMODEFLAG, planmodeflag);
//        db.insert(TABLE_PLANCODETABLERDUPDATE, null, contentValues);
//        db.close();
//        return true;
//    }
    public boolean updateRD() {
//        String countQuery = "SELECT  * FROM " + TABLE_PLANTABLE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLANTABLE);
        return true;
    }
//
//    public Cursor getplancoderd() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from " + TABLE_PLANCODETABLERDUPDATE, null);
//        return res;
//    }
    public boolean InsertFD(String plancodefd,String plancodef) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_PLANCODEFD, plancodefd);
        contentValues.put(TABLE_PLNCODEFD, plancodef);
        db.insert(TABLE_PLANCODETABLEFD, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getplancodefd() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PLANCODETABLEFD, null);
        return res;
    }

    public boolean InsertMIS(String plancodemis,String planmis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_PLANCODEMIS, plancodemis);
        contentValues.put(TABLE_PLNCODEMIS, planmis);
        db.insert(TABLE_PLANCODETABLEMIS, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getplancodeMIS() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PLANCODETABLEFD, null);
        return res;
    }
    /////////////////////////////////////Insert New Account //////////////////////////////
    public boolean NewAccountInsert(String newAcBranch,String newAcDateOfEnt,
                                    String newAcMemeberCode,String newAcApplicantName,String newAcFather,String newAcAddress,String newAcPhoneNo,
                                    String newAcAccountType,String newAcJointApplicationName,String newAcJointFather,String newAcJointAddress,String newAcAdvisorCode,String newAcAmount,String newAcPayMode,String newAcUserId,String newAcSbAccount,int rdStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWACBRANCH,newAcBranch);
        contentValues.put(TABLE_NEWACDATEOFENT,newAcDateOfEnt);
        contentValues.put(TABLE_NEWACMEMBERCODE,newAcMemeberCode);
        contentValues.put(TABLE_NEWACAPPLICANTNAME,newAcApplicantName);
        contentValues.put(TABLE_NEWACFATHER,newAcFather);
        contentValues.put(TABLE_NEWACADDRESS,newAcAddress);
        contentValues.put(TABLE_NEWACPHONENO,newAcPhoneNo);
        contentValues.put(TABLE_NEWACACCOUNTTYPE,newAcAccountType);
        contentValues.put(TABLE_NEWACJOINTAPPLICANTNAME,newAcJointApplicationName);
        contentValues.put(TABLE_NEWACJOINTFATHER,newAcJointFather);
        contentValues.put(TABLE_NEWACJOINTADDRESS,newAcJointAddress);
        contentValues.put(TABLE_NEWACADVISORCODE,newAcAdvisorCode);
        contentValues.put(TABLE_NEWACAMOUNT,newAcAmount);
        contentValues.put(TABLE_NEWACPAYMODE,newAcPayMode);
        contentValues.put(TABLE_NEWACUSERID,newAcUserId);
        contentValues.put(TABLE_NEWACSBACCOUNT,newAcSbAccount);
        contentValues.put(TABLE_NEWACSTATUS,rdStatus);
        db.insert(TABLE_NEWACDATATABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean NewAccountUpdate(String newAcId,String newAcBranch,String newAcDateOfEnt,
                                    String newAcMemeberCode,String newAcApplicantName,String newAcFather,String newAcAddress,String newAcPhoneNo,
                                    String newAcAccountType,String newAcJointApplicationName,String newAcJointFather,String newAcJointAddress,String newAcAdvisorCode,String newAcAmount,String newAcPayMode,String newAcUserId,String newAcSbAccount,int rdStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWACID,newAcId);
        contentValues.put(TABLE_NEWACBRANCH,newAcBranch);
        contentValues.put(TABLE_NEWACDATEOFENT,newAcDateOfEnt);
        contentValues.put(TABLE_NEWACMEMBERCODE,newAcMemeberCode);
        contentValues.put(TABLE_NEWACAPPLICANTNAME,newAcApplicantName);
        contentValues.put(TABLE_NEWACFATHER,newAcFather);
        contentValues.put(TABLE_NEWACADDRESS,newAcAddress);
        contentValues.put(TABLE_NEWACPHONENO,newAcPhoneNo);
        contentValues.put(TABLE_NEWACACCOUNTTYPE,newAcAccountType);
        contentValues.put(TABLE_NEWACJOINTAPPLICANTNAME,newAcJointApplicationName);
        contentValues.put(TABLE_NEWACJOINTFATHER,newAcJointFather);
        contentValues.put(TABLE_NEWACJOINTADDRESS,newAcJointAddress);
        contentValues.put(TABLE_NEWACADVISORCODE,newAcAdvisorCode);
        contentValues.put(TABLE_NEWACAMOUNT,newAcAmount);
        contentValues.put(TABLE_NEWACPAYMODE,newAcPayMode);
        contentValues.put(TABLE_NEWACUSERID,newAcUserId);
        contentValues.put(TABLE_NEWACSBACCOUNT,newAcSbAccount);
        contentValues.put(TABLE_NEWACSTATUS,rdStatus);;
        db.update(TABLE_NEWACDATATABLE, contentValues, "insertNewAccountid = ?",new String[] { newAcId });
        db.close();
        return true;
    }
    public Cursor InsertNewAccountValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NEWACDATATABLE, null);
        return res;
    }
    public boolean updateNewAccountValue(int newAcId, int statusNA) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWACSTATUS,statusNA);
        db.update(TABLE_NEWACDATATABLE, contentValues, TABLE_NEWACID + "=" + newAcId, null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedInsertNA() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NEWACDATATABLE + " WHERE " + TABLE_NEWACSTATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    //////////////////////////////////////////Insert Account Transaction //////////////////////
    public boolean AccountTransactionInsert(String atbranch,String atsbaccount,
                                            String ataccountno,String atpayeename,String attrantype,String atamount,String attdate,
                                            String atuserid,String atremarks,String atstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ATBRANCH,atbranch);
        contentValues.put(TABLE_ATSBACCOUNT,atsbaccount);
        contentValues.put(TABLE_ATACCOUNTNO,ataccountno);
        contentValues.put(TABLE_ATPAYEENAME,atpayeename);
        contentValues.put(TABLE_ATTRANTYPE,attrantype);
        contentValues.put(TABLE_ATAMOUNT,atamount);
        contentValues.put(TABLE_ATTDATE,attdate);
        contentValues.put(TABLE_ATUSERID,atuserid);
        contentValues.put(TABLE_ATREMARKS,atremarks);
        contentValues.put(TABLE_ATSTATUS,atstatus);
        db.insert(TABLE_ACCOUNTTRANSACTION, null, contentValues);
        db.close();
        return true;
    }
    public boolean AccountTransactionUpdate(String atid,String atbranch,String atsbaccount,
                                            String ataccountno,String atpayeename,String attrantype,String atamount,String attdate,
                                            String atuserid,String atremarks,String atstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ACCOUNTTRANSACTIONID,atid);
        contentValues.put(TABLE_ATBRANCH,atbranch);
        contentValues.put(TABLE_ATSBACCOUNT,atsbaccount);
        contentValues.put(TABLE_ATACCOUNTNO,ataccountno);
        contentValues.put(TABLE_ATPAYEENAME,atpayeename);
        contentValues.put(TABLE_ATTRANTYPE,attrantype);
        contentValues.put(TABLE_ATAMOUNT,atamount);
        contentValues.put(TABLE_ATTDATE,attdate);
        contentValues.put(TABLE_ATUSERID,atuserid);
        contentValues.put(TABLE_ATREMARKS,atremarks);
        contentValues.put(TABLE_ATSTATUS,atstatus);
        db.update(TABLE_ACCOUNTTRANSACTION, contentValues, "insertAccountTransactionid = ?",new String[] { atid });
        db.close();
        return true;
    }
    public Cursor InsertAccountTransactionValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ACCOUNTTRANSACTION, null);
        return res;
    }
    public boolean updateAccountTransactionValue(int atid, int statusAT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWACSTATUS,statusAT);
        db.update(TABLE_ACCOUNTTRANSACTION, contentValues, TABLE_ACCOUNTTRANSACTIONID + "=" + atid, null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedAccountTransactionValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NEWACDATATABLE + " WHERE " + TABLE_NEWACSTATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    /////////////////////////////////Insert Loan //////////////////////////////////////////
    public boolean LoanInsert(String loanbranchname,
                              String loanmembercode,String loanmembername,
                              String loanfathername,String loanaddress,String loanregdate,String loanscheme,String loanterm,
                              String loanmode,String loanroi,String loaninterestmode,String loanamount,String loanemiamount,String loancollectorcode,String loanpurpose,String loanuserid,int loanstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWLOANBRANCH,loanbranchname);
        contentValues.put(TABLE_NEWLOANMEMBERCODE,loanmembercode);
        contentValues.put(TABLE_NEWLOANMEMBERNAME,loanmembername);
        contentValues.put(TABLE_NEWLOANFATHERNAME,loanfathername);
        contentValues.put(TABLE_NEWLOANADDRESS,loanaddress);
        contentValues.put(TABLE_NEWLOANREGDATE,loanregdate);
        contentValues.put(TABLE_NEWLOANSCHEME,loanscheme);
        contentValues.put(TABLE_NEWLOANTERM,loanterm);
        contentValues.put(TABLE_NEWLOANMODE,loanmode);
        contentValues.put(TABLE_NEWLOANROI,loanroi);
        contentValues.put(TABLE_NEWLOANINTERESTMODE,loaninterestmode);
        contentValues.put(TABLE_NEWLOANLOANAMOUNT,loanamount);
        contentValues.put(TABLE_NEWLOANEMIAMOUNT,loanemiamount);
        contentValues.put(TABLE_NEWLOANCOLLECTORCODE,loancollectorcode);
        contentValues.put(TABLE_NEWLOANPURPOSE,loanpurpose);
        contentValues.put(TABLE_NEWLOANUSERID,loanuserid);
        contentValues.put(TABLE_NEWLOANSTATUS,loanstatus);
        db.insert(TABLE_NEWLOANTABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean LoanUpdate(String loanid,String loanbranchname,
                              String loanmembercode,String loanmembername,
                              String loanfathername,String loanaddress,String loanregdate,String loanscheme,String loanterm,
                              String loanmode,String loanroi,String loaninterestmode,String loanamount,String loanemiamount,String loancollectorcode,String loanpurpose,String loanuserid,int loanstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWLOANBRANCH,loanbranchname);
        contentValues.put(TABLE_NEWLOANMEMBERCODE,loanmembercode);
        contentValues.put(TABLE_NEWLOANMEMBERNAME,loanmembername);
        contentValues.put(TABLE_NEWLOANFATHERNAME,loanfathername);
        contentValues.put(TABLE_NEWLOANADDRESS,loanaddress);
        contentValues.put(TABLE_NEWLOANREGDATE,loanregdate);
        contentValues.put(TABLE_NEWLOANSCHEME,loanscheme);
        contentValues.put(TABLE_NEWLOANTERM,loanterm);
        contentValues.put(TABLE_NEWLOANMODE,loanmode);
        contentValues.put(TABLE_NEWLOANROI,loanroi);
        contentValues.put(TABLE_NEWLOANINTERESTMODE,loaninterestmode);
        contentValues.put(TABLE_NEWLOANLOANAMOUNT,loanamount);
        contentValues.put(TABLE_NEWLOANEMIAMOUNT,loanemiamount);
        contentValues.put(TABLE_NEWLOANCOLLECTORCODE,loancollectorcode);
        contentValues.put(TABLE_NEWLOANPURPOSE,loanpurpose);
        contentValues.put(TABLE_NEWLOANUSERID,loanuserid);
        contentValues.put(TABLE_NEWLOANSTATUS,loanstatus);
        db.update(TABLE_NEWLOANTABLE, contentValues, "insertLoanid = ?",new String[] { loanid });
        db.close();
        return true;
    }
    public Cursor InsertLoanValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NEWLOANTABLE, null);
        return res;
    }
    public boolean updateLoanValue(int insertLoanid, int statusLoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NEWLOANSTATUS,statusLoan);
        db.update(TABLE_NEWLOANTABLE, contentValues, TABLE_NEWLOANID + "=" + insertLoanid, null);
        db.close();
        return true;
    }
    public Cursor getUnsyncedLoan() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NEWLOANTABLE + " WHERE " + TABLE_NEWLOANSTATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public boolean ModeRD12(String mode,String term,String roi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_MODE, mode);
        contentValues.put(TABLE_TERM, term);
        contentValues.put(TABLE_ROI, roi);
        db.insert(TABLE_RD12TABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean ModeFD12(String modefd,String termfd,String roifd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_MODEFD, modefd);
        contentValues.put(TABLE_TERMFD, termfd);
        contentValues.put(TABLE_ROIFD, roifd);
        db.insert(TABLE_FD12TABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean MIS60(String modemis,String termmis,String roimis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_MODEFDMIS, modemis);
        contentValues.put(TABLE_TERMMIS, termmis);
        contentValues.put(TABLE_ROIMIS, roimis);
        db.insert(TABLE_MIS60TABLE, null, contentValues);
        db.close();
        return true;
    }
}
