package app.group;

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
@Table(name="Group_table")
public class Group  extends AbstractEvent{

    @Id
    private int groupId;

    @Column(length = 100)
    private String groupName;

    @Column(length = 100)
    private String programId;

    @Column(length = 500)
    private String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private Date updateDate;

    private String createUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private Date createDate;
}
