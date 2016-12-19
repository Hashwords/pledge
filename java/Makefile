
LIB=Pledge
SHARED_LIBS=Pledge
SHLIB_MAJOR=1
SHLIB_MINOR=0
LIBDIR=/usr/local/lib
LINKS=${LIBDIR}/lib${LIB}.so.${SHLIB_MAJOR}.${SHLIB_MINOR} \
${LIBDIR}/lib${LIB}.so

HW_DIR=org/hashwords/pledge
HW_PATH_TMP!=echo ${HW_DIR} |  sed -e 's/\//./g'
HW_JAR=${HW_PATH_TMP}.jar
HW_PATH=${HW_PATH_TMP}.${SHARED_LIBS}
HW_HDRS!=echo ${HW_PATH} |  sed -e 's/\./_/g'
HDRS=${HW_HDRS}.h
SRCS=Pledge.c
INCLUDES=-I${JAVA_HOME}/include/ -I${JAVA_HOME}/include/openbsd/
CFLAGS+=${INCLUDES}
CLEANFILES+=${HW_DIR}/Pledge.class ${HDRS} ${HW_JAR}

includes:
	${JAVA_HOME}/bin/javah ${HW_PATH}

all: depends includes

depends:

.if defined(JAVA_HOME)
	@echo "JAVA_HOME="${JAVA_HOME}
.else
	@echo "JAVA_HOME must be set"
	@false
.endif

	${JAVA_HOME}/bin/javac ${HW_DIR}/Pledge.java
	${JAVA_HOME}/bin/jar cf ${HW_JAR} ${HW_DIR}/Pledge.class

afterinstall:
	install -c -S -o root -g bin -m 444 ${HW_JAR} ${JAVA_HOME}/jre/lib/ext

.include <bsd.lib.mk>
