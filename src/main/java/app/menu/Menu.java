package app.menu;

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
@Table(name="Menu_table")
public class Menu  extends AbstractEvent{

    @Id
    private int menuId;

    @Column(nullable = true)
    private int parentId;

    @Column(length = 100)
    private String menuName;

    @Column(length = 500)
    private String routeName;

    private String updateUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private Date updateDate;

    private String createUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private Date createDate;
}
