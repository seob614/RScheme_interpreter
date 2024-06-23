package com.example.rscheme_interpreter;

public class Rs {
    private String homePath;
    private String[] args;

    public Rs() {
        this.args = new String[1];
        this.args[0] = this.getClass().getSimpleName();
        this.homePath = "";
    }

    public void init(String homePath, String args) {
        String src[] = args.split(",");
        init(homePath, src);
    }

    public void init(String homePath, String[] argv) {
        int len = this.args.length;
        String[] newArgs = new String[argv.length + 1];
        System.arraycopy(this.args, 0, newArgs, 0, len);
        System.arraycopy(argv, 0, newArgs, len, argv.length);
        this.args = newArgs;
        this.homePath = homePath;
        //System.load("C:\\Users\\USER\\AndroidStudioProjects\\RScheme_interpreter\\app\\libs\\armeabi-v7a");
        System.loadLibrary("myrs");
        init();

    }

    public native void init();

    public native String eval(String exp);

    public native void exit();
}
