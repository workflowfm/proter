FROM adoptopenjdk:8u292-b10-jre-openj9-0.26.0-focal

WORKDIR /usr/app/proteronline

ADD src/ /usr/app/proteronline/src
ADD build.sbt /usr/app/proteronline
ADD project/build.properties /usr/app/proteronline/project/build.properties
ADD project/Dependencies.scala /usr/app/proteronline/project/Dependencies.scala
ADD project/plugins.sbt /usr/app/proteronline/project/plugins.sbt

RUN ls /usr/app/proteronline

RUN apt-get update
RUN apt-get -y install gnupg
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update
RUN apt-get -y install sbt

RUN sbt test

CMD sbt run