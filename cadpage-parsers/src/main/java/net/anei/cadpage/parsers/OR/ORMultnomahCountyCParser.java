package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORMultnomahCountyCParser extends FieldProgramParser {

  public ORMultnomahCountyCParser() {
    super("MULTNOMAH COUNTY", "OR", 
          "CALL ADDR CITY? ID? INFO! INFO+");
  }

  private static Pattern CALL_COLON_PTN = Pattern.compile("(\\d+):(.*)", Pattern.DOTALL);
  private static Pattern BODY_ID_INFO = Pattern.compile("(.*;\\d{2}-\\d ?\\d{2})((?!;).*)", Pattern.DOTALL);
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Fix some common typos
    Matcher mat = CALL_COLON_PTN.matcher(body);
    if (mat.matches()) body = mat.group(1) + ';' + mat.group(2);
    mat = BODY_ID_INFO.matcher(body);
    if (mat.matches()) body = mat.group(1) + ';' + mat.group(2);
    
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("911|411|119|Mission.*", true);
    if (name.equals("ID")) return new IdField("\\d{2}-\\d ?\\d{2}", false);
    if (name.equals("CITY")) return new CityField("(?!\\d).*", false);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  private static Pattern DATE_TIME =            Pattern.compile("(\\d{1,2}[\\.\\-]\\d{1,2}[\\.\\-](?:\\d{4}|\\d{2}))?[ @]+(\\d{1,2}:\\d{2})?[,\\. ]*");
  private static Pattern DATE_DELIM = Pattern.compile("[\\.\\-]");
  private class MyInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      //look for date/time by @ sign
      Matcher mat = DATE_TIME.matcher(field);
      while (mat.find()) {
        String date = mat.group(1);
        String time = mat.group(2);
        
        //if neither date nor time actually matched, continue to next match
        if (date == null && time == null) continue; 
        
        //remove construct from info
        data.strSupp = append(field.substring(0, mat.start()).trim(), " ", field.substring(mat.end(), field.length()).trim());
        //clean up and assign DATE if not null
        if (date != null) data.strDate = DATE_DELIM.matcher(date).replaceAll("/");
        //TIME
        data.strTime = getOptGroup(time);
        return;
      }
      
      //if no safe pattern match, put it all in info
      data.strSupp = field;
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
