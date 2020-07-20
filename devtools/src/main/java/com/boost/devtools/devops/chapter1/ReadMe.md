###  install Gitlab
#### 用docker安装Gitlab
+ docker pull gitlab-ce
+ docker run -d  -p 443:443 -p 80:80 -p 222:22 --name gitlab --restart always -v /home/gitlab/config:/etc/gitlab -v /home/gitlab/logs:/var/log/gitlab -v /home/gitlab/data:/var/opt/gitlab gitlab/gitlab-ce
+ 修改配置文件
```
external_url 'http://192.168.124.128:8100' //宿主机ip
gitlab_rails['smtp_enable'] = true
gitlab_rails['smtp_address'] = "smtp.126.com"
gitlab_rails['smtp_port'] = 465
gitlab_rails['smtp_user_name'] = "liand1@126.com"
gitlab_rails['smtp_password'] = "MNLHDIYRMXLIHDTQ" // 邮箱smtp授权码, 126发现没有经常使用会关闭掉，所以如果邮件发送有问题，需要检查时候关闭了smtp
gitlab_rails['smtp_domain'] = "126.com"
gitlab_rails['smtp_authentication'] = "login"
gitlab_rails['smtp_enable_starttls_auto'] = true
gitlab_rails['smtp_tls'] = true
gitlab_rails['gitlab_email_from'] = "liand1@126.com"
gitlab_rails['gitlab_email_reply_to']= "noreply@126.com"
user['git_user_email'] = "liand1@126.com"
```
+ gitlab-ctl reconfigure // 重新配置
+ 测试是否配置成功
```
gitlab-rails console
Notify.test_email('357928825@qq.com', 'Message Subject123', 'Message Body123').deliver_now
```
+ gitlab-rake gitlab:check SANITIZE=true // 检查gitlab所有组件是否运行正常


