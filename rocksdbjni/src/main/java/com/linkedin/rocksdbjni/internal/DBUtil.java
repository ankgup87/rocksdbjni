package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import java.io.File;
import java.io.IOException;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;
import static org.fusesource.hawtjni.runtime.ArgFlag.*;

/**
 * Some miscellaneous utility functions.
 * 
 * @author Ankit Gupta
 */
public class DBUtil
{

  @JniClass(name = "rocksdb::Env", flags = { CPP })
  static class EnvJNI
  {

    static
    {
      NativeDB.LIBRARY.load();
    }

    @JniMethod(cast = "rocksdb::Env *", accessor = "rocksdb::Env::Default")
    public static final native long Default();

    @JniMethod(flags = { CPP_METHOD })
    public static final native void Schedule(long self,
                                             @JniArg(cast = "void (*)(void*)") long fp,
                                             @JniArg(cast = "void *") long arg);

  }

  @JniClass(flags = { CPP })
  static class UtilJNI
  {

    static
    {
      NativeDB.LIBRARY.load();
      init();
    }

    @JniMethod(flags = { CONSTANT_INITIALIZER })
    private static final native void init();

    @JniField(flags = { CONSTANT }, accessor = "1", conditional = "defined(_WIN32) || defined(_WIN64)")
    static int ON_WINDOWS;

    @JniMethod(conditional = "!defined(_WIN32) && !defined(_WIN64)")
    static final native int link(@JniArg(cast = "const char*") String source,
                                 @JniArg(cast = "const char*") String target);

    @JniMethod(conditional = "defined(_WIN32) || defined(_WIN64)")
    static final native int CreateHardLinkW(@JniArg(cast = "LPCWSTR", flags = { POINTER_ARG, UNICODE }) String target,
                                            @JniArg(cast = "LPCWSTR", flags = { POINTER_ARG, UNICODE }) String source,
                                            @JniArg(cast = "LPSECURITY_ATTRIBUTES", flags = { POINTER_ARG }) long lpSecurityAttributes);

    @JniMethod(flags = { CONSTANT_GETTER })
    public static final native int errno();

    @JniMethod(cast = "char *")
    public static final native long strerror(int errnum);

    public static final native int strlen(@JniArg(cast = "const char *") long s);

  }

  static int errno()
  {
    return UtilJNI.errno();
  }

  static String strerror()
  {
    return string(UtilJNI.strerror(errno()));
  }

  static String string(long ptr)
  {
    if (ptr == 0)
      return null;
    return new String(new NativeSlice(ptr, UtilJNI.strlen(ptr)).toByteArray());
  }

}
