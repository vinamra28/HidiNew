package com.example.hp.hidi2;

/**
 * Created by HP on 08-Apr-18.
 */

public class BlocksGet
{
    int block_uid;
    String secname,user_pic;

    public BlocksGet(int block_uid, String secname, String user_pic)
    {
        this.block_uid = block_uid;
        this.secname = secname;
        this.user_pic = user_pic;
    }

    public int getBlock_uid() {
        return block_uid;
    }

    public void setBlock_uid(int block_uid) {
        this.block_uid = block_uid;
    }

    public String getSecname() {
        return secname;
    }

    public void setSecname(String secname) {
        this.secname = secname;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }
}
