package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Stafford, TX
 */
public class TXStaffordParser extends FieldProgramParser {
  
  public TXStaffordParser() {
    super(CITY_LIST, "STAFFORD", "TX",
        "CALL CALL ADDR/S BOX? BOX? BOX? INFO+");
  }
  
  public String getFilter() {
    return "cad_alerts@staffordpd.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replaceAll("\\b([NWES])/B\\b", "$1B");
    return parseFields(body.split("/", 7), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("BOX")) return new MyBoxField("[A-Z0-9]{2}", true);
    return super.getField(name);
  }
    
  private class MyBoxField extends BoxField {
    MyBoxField(String p, boolean h) {
      super(p, h);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("OJ")) return;
      data.strBox = append(data.strBox, "-", field);
    }
  }
  
  private static final Pattern MAP_PATTERN
    = Pattern.compile("(?:LOCATION NOTES:|K(?:M|EYMAP))(.*)");
  private static final Pattern CROSS_PATTERN
    = Pattern.compile("(?:C ?/? ?S|XSTREET)(.*)");
  private static final Pattern HYPHEN_PATTERN
    = Pattern.compile("-+");
  private static final Pattern APT_PATTERN
    = Pattern.compile("(?:ap(?:t|artment)|n(?:o\\.?|umber)) +(\\S+).*", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String[] piece = field.split("\n");
      for (int p=0; p<piece.length; p++)
        parsePiece(piece[p].trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" MAP X APT";
    }
    
    private void parsePiece(String piece, Data data) {
      Matcher m = MAP_PATTERN.matcher(piece);
      if (m.matches()) {
        data.strMap = append(data.strMap, "-", m.group(1).trim());
        return;
      }
      m = CROSS_PATTERN.matcher(piece);
      if (m.matches()) {
        data.strCross = m.group(1).replaceAll(" +(?:\\&|AND) +", "/").trim();
        return;
      }
      m = HYPHEN_PATTERN.matcher(piece);
      if (m.matches())
        return;
      m = APT_PATTERN.matcher(piece);
      if (m.matches()) {
        data.strApt = m.group(1).trim();
        return;
      }
      data.strSupp = append(data.strSupp, "/", piece.trim());
    }
  }
  
  private static final String[] CITY_LIST = {
    "STAFFORD",
    "MISSOURI CITY",
    "SUGARLAND"
  };
}
