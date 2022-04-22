package app.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.com.kafka.AbstractEvent;
import lombok.Data; 

@Data
@Entity
@Table(name="User_table")
public class User  extends AbstractEvent{

    @Id
    @Column(length = 30)
    private String userId;

    @Column(length = 100, nullable = false)
    private String userPwd;

    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String nickName;

    @Column(length = 50)
    private String officeTelNo;
    
    private String fileId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private Date updateDate;

    private String createUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private Date createDate;


}
