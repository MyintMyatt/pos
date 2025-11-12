FROM alpine:latest

RUN apk update && apk add --no-cache curl dos2unix

COPY init.sh /init.sh
RUN dos2unix /init.sh && chmod +x /init.sh

ENTRYPOINT ["/bin/sh", "/init.sh"]