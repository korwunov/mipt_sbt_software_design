FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive

RUN apt -y update && apt -y upgrade && apt -y install openjdk-17-jdk maven openssh-server git
RUN mkdir /var/run/sshd

RUN useradd -m -s /bin/bash admin
RUN echo 'admin:securepassword' | chpasswd

# Добавление пользователя в sudoers
RUN echo 'admin ALL=(ALL) ALL' >> /etc/sudoers
RUN usermod -a -G sudo admin

# Настройка SSH
RUN sed -i 's/#PermitRootLogin yes/PermitRootLogin no/' /etc/ssh/sshd_config
RUN sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config
RUN ssh-keygen -A

# Настройка ключей SSH
RUN mkdir /home/admin/.ssh
# COPY authorized_keys /home/admin/.ssh/
RUN chown -R admin:admin /home/admin/.ssh
RUN chmod 700 /home/admin/.ssh
# RUN chmod 600 /home/admin/.ssh/authorized_keys

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]