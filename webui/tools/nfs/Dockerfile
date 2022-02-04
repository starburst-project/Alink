FROM itsthenetwork/nfs-server-alpine:12

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories

COPY exports /etc/
