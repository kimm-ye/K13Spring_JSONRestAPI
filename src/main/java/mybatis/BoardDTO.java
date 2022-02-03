package mybatis;

import lombok.Data;

@Data
public class BoardDTO {
	
	private String num;
	private String title;
	private String content;
	private String id;
	//private java.sql.Date postdate;
	private String postdate; //리액트에서는 위와 같이 작성하면 숫자만 나오기 때문에 String으로 바꾸면 된다.
	private String visitcount;
	
	
}
