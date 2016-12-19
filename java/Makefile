
LIB=Pledge
SHLIB_MAJOR=1
SHLIB_MINOR=0
LIBDIR=/usr/local/lib
LINKS=${LIBDIR}/lib${LIB}.so.${SHLIB_MAJOR}.${SHLIB_MINOR} \
${LIBDIR}/lib${LIB}.so

SHARED_LIBS= Pledge
HDRS=Pledge.h
SRCS=Pledge.c
INCLUDES=-I${JAVA_HOME}/include/ -I${JAVA_HOME}/include/openbsd/
CFLAGS+=${INCLUDES}
CLEANFILES+=Pledge.class Pledge.h

includes:
	${JAVA_HOME}/bin/javah Pledge

all: depends includes

depends:

.if defined(JAVA_HOME)
	@echo "JAVA_HOME="${JAVA_HOME}
.else
	@echo "JAVA_HOME must be set"
	@false
.endif

	${JAVA_HOME}/bin/javac Pledge.java

.include <bsd.lib.mk>
