package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PAMonroeCountyBParser extends HtmlProgramParser {
  
  public PAMonroeCountyBParser() {
    super("MONROE COUNTY", "PA",
          "CALL! Priority:PRI? ( STARS! INFO/N+? STARS! PLACE? ADDRCITY/ZS6 X_STS:X! GPS! STARS! INFO/N+? STARS! Your_INC#:ID ID/S+? STARS! TIMES/N+ " +
                              "| ( ADDRCITY/Z X_STS:X | CALL/SDS+? DESC ADDRCITY/ZS6! X_STS:X! ) GPS! INFO/N+ INC#:ID! TIMES/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "notify@monroeco911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  String times;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    times = "";
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(data.strSupp, "\n", times);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DESC")) return new MyDescField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("STARS")) return new SkipField("\\*{10,}");
    return super.getField(name);
  }
  
  private Pattern END_APT_LABEL_PTN = Pattern.compile("(.*?) +(?:#?APT:?|#|#?LOT|RM|ROOM:?|SP|STE|SUITE|UNIT)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strApt.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, data.strApt);
        Matcher match = END_APT_LABEL_PTN.matcher(data.strCity);
        if (match.matches()) data.strCity = match.group(1);
      }
    }
  }
  
  private class MyDescField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(+1))) return;
      super.parse(field, data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('/', ',');
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d:\\d\\d:\\d\\d");
  private static final Pattern INFO_HEAD_PTN = Pattern.compile("monroeco911[ \\\\][a-z]+ - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      Matcher match = INFO_HEAD_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Cleared:")) data.msgType = MsgType.RUN_REPORT;
      times = append(times, "\n", field);
    }
  }
}
