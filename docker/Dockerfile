
FROM ubuntu:jammy

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && \
    apt-get install -q -y \
    openjdk-17-jdk openjfx ant maven \
    dpkg-dev debhelper make \
    git sox timidity curl locales iputils-ping file && \
    rm -rf /var/lib/apt/lists/* && \
    sed -i '/ru_RU.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen

ENV LC_ALL ru_RU.UTF-8
ENV LANG ru_RU.UTF-8
ENV LANGUAGE ru_RU.UTF-8


WORKDIR /wrk
COPY lwr-build-deb /wrk/lwr-build-deb
CMD [ "./lwr-build-deb" ]