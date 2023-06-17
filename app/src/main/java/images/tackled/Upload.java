package images.tackled;

public class Upload {

    private String mName;
    private String mImageurl;
    public Upload(){

    }
    public Upload(String mName,String mImageurl){
        if(mName.trim().equals(""))
            mName="No name";

    }

    public String getmName() {
        return mName;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmImageurl(String mImageurl) {
        this.mImageurl = mImageurl;
    }
}

