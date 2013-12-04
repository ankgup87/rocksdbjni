
AC_DEFUN([CUSTOM_M4_SETUP],
[
  AC_LANG_PUSH(C++)

  AC_CHECK_HEADER([pthread.h],[AC_DEFINE([HAVE_PTHREAD_H], [1], [Define to 1 if you have the <pthread.h> header file.])])

  AC_ARG_WITH([rocksdb],
  [AS_HELP_STRING([--with-rocksdb@<:@=PATH@:>@],
    [Directory where rocksdb was built. Example: --with-rocksdb=/opt/rocksdb])],
  [
    CFLAGS="$CFLAGS -I${withval}/include"
    CXXFLAGS="$CXXFLAGS -I${withval}/include"
    AC_SUBST(CXXFLAGS)
    LDFLAGS="$LDFLAGS -lrocksdb -L${withval}"
    AC_SUBST(LDFLAGS)
  ])


  AC_ARG_WITH([snappy],
  [AS_HELP_STRING([--with-snappy@<:@=PATH@:>@],
    [Directory where snappy was built. Example: --with-snappy=/opt/snappy])],
  [
    LDFLAGS="$LDFLAGS -lsnappy -L${withval}"
    AC_SUBST(LDFLAGS)
  ])

  AC_CHECK_HEADER([sys/errno.h],[AC_DEFINE([HAVE_SYS_ERRNO_H], [1], [Define to 1 if you have the <sys/errno.h> header file.])])

  AC_LANG_POP()
])