Hướng dẫn cấu hình đăng nhập bằng database của sakai:

Sửa file app-config.propertites đổi tên database từ sakai -->sakai233


Sửa file spring-security_cas_local.xml line 17-18 đổi lớp cấu hình mã hóa của sakai 
	<!-- Password Encoder -->
    <beans:bean id="passwordEncoder" 
          class="mks.myworkspace.cvhub.service.impl.EncodePasswordImpl"/>
	Line 24 đổi tên database từ sakai -->sakai233
	Từ Line 54 đến 74 của phần <authentication-manager> </authentication-manager> đổi 2 câu lệnh sql 
	users-by-username-query="
                    SELECT sim.EID AS username, su.PW AS password, 1 AS enabled
                    FROM sakai_user_id_map sim
                    JOIN sakai_user su ON sim.USER_ID = su.USER_ID
                    WHERE sim.EID = ?"
    authorities-by-username-query="
                    SELECT sim.EID AS username,  'ROLE_USER' as authority
                    FROM sakai_user_id_map sim
                    JOIN sakai_user su ON sim.USER_ID = su.USER_ID
                    WHERE sim.EID = ?"

   Với điều kiện: Phải chạy được sakai, có database của sakai233.
   
   
Nếu muốn đăng ký tài khoản có gửi xác nhận qua mail thì trong database sakai đến table cvhub_user thêm cột status. 
Khi đăng ký thì nó sẽ tạo ra account status ở trạng thái null, sau đó gửi mail xác nhận verify thì sẽ ra trạng thái accept, và tôi đã tạo ra file cấu hình đăng nhập  spring-security_cas_local2.xml tức là chỉ đăng nhập với các account có status là accept
<password-encoder ref="passwordEncoder"/>
            <jdbc-user-service data-source-ref="dataSource"
                 users-by-username-query="select email as username, password, 1 as enabled from cvhub_user where email=? and status='accept'"
                authorities-by-username-query="select email as username, role as authority from cvhub_user where email=?"
            />
Với điều kiện trong file cấu hình app-config.properties có register.user.confirm = true