
FROM ubuntu:jammy

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && \
    apt-get install -q -y \
    squashfs-tools \
    openjdk-17-jdk openjfx ant maven \
    dpkg-dev debhelper make \
    sox timidity midicsv \
    git curl wget locales iputils-ping file && \
    rm -rf /var/lib/apt/lists/* && \
    sed -i '/ru_RU.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen

ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64

ENV LC_ALL ru_RU.UTF-8
ENV LANG ru_RU.UTF-8
ENV LANGUAGE ru_RU.UTF-8

WORKDIR /wrk
COPY lwr-build-iso /wrk/lwr-build-iso
CMD [ "./lwr-build-iso" ]