FROM daflockinger/archjava8:v0.1
MAINTAINER Florian Mitterbauer

RUN pacman -Sy --noconfirm unzip

WORKDIR /spongeblog

ADD target/universal/spongeblog-0.1.0.zip /spongeblog/
ADD conf/application.production.conf /spongeblog/

RUN unzip spongeblog-0.1.0.zip

EXPOSE 9000

ENTRYPOINT spongeblog-0.1.0/bin/spongeblog -Dconfig.file=/spongeblog/application.production.conf -Dplay.crypto.secret="8uKdLfn_r67kbWTh_INshsdkfljhsdlfkdsh0JZtmYMpwIy7PKEiMGZ73rAV@pJMWnugzL"
