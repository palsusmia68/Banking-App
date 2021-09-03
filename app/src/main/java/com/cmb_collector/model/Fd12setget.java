package com.cmb_collector.model;

public class Fd12setget {
    private String fdtwelvetableid;
    private String modefd;
    private String termfd;
    private String roifd;
    public Fd12setget(String fdtwelvetableid,String modefd,String termfd,String roifd){
        this.fdtwelvetableid=fdtwelvetableid;
        this.modefd=modefd;
        this.termfd=termfd;
        this.roifd=roifd;
    }

    public String getFdtwelvetableid() {
        return fdtwelvetableid;
    }

    public void setFdtwelvetableid(String fdtwelvetableid) {
        this.fdtwelvetableid = fdtwelvetableid;
    }

    public String getModefd() {
        return modefd;
    }

    public void setModefd(String modefd) {
        this.modefd = modefd;
    }

    public String getTermfd() {
        return termfd;
    }

    public void setTermfd(String termfd) {
        this.termfd = termfd;
    }

    public String getRoifd() {
        return roifd;
    }

    public void setRoifd(String roifd) {
        this.roifd = roifd;
    }
}
