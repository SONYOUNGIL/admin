package app.user;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	 public User selectUser(Map<String, Object> paramMap);
}