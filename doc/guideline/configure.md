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