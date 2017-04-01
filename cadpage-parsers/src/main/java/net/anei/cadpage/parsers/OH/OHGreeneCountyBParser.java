package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGreeneCountyBParser extends HtmlProgramParser {
  
  public OHGreeneCountyBParser() {
    super("GREENE COUNTY", "OH", 
          "Call:CODE_CALL! Place:ADDRCITY/SP! Cross:X! ID:ID! PRI:PRI! Date:DATETIME! Map:MAP! Units:UNIT! Narrative:INFO/N+");
    setupMultiWordStreets(MULTI_WORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@xi.xenia.oh.us,@ci.xenia.oh.us,@beavercreekohio.gov,@ci.fairborn.oh.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]{3,5})(?:-|  ) *(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      field = stripFieldEnd(field, " CD");
      super.parse(field, data);
      data.strAddress = stripFieldEnd(data.strAddress, " CD");
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+|\\d\\d:\\d\\d:\\d\\d|[a-z]+|-");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] MULTI_WORD_STREET_LIST = new String[]{
      "APPLE GROVE",
      "BIG WOODS",
      "BROWN BARK",
      "CENTER POINT",
      "CLEAR VIEW",
      "COLONEL GLENN",
      "CORNERSTONE NORTH",
      "COUNTY LINE",
      "DAYTON SPRINGFIELD",
      "DAYTON XENIA",
      "DAYTON YELLOW SPRINGS",
      "EL CAMINO",
      "FAIRFIELD COMMONS",
      "FOREST DELL",
      "GRAND PORTAGE",
      "GRANGE HALL",
      "GRANGE VIEW",
      "HONEY JANE",
      "INDIAN RIPPLE",
      "MEADOW BRIDGE",
      "NORTH HAVEN",
      "PINE BLUFF",
      "RED APPLE",
      "RED OAK",
      "SOUTH POINT",
      "SPRING VALLEY ALPHA",
      "STRAIGHT ARROW",
      "TOLL GATE",
      "VALLEY OAK",
      "VAN TRESS",
      "VILLAGE GREEN",
      "WRIGHT CYCLE",
      "YELLOW ROSE",
      "YELLOW SPRINGS FAIRFIELD"
     
  };
}
