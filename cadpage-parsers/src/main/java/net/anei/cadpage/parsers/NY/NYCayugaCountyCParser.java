package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYCayugaCountyCParser extends FieldProgramParser {
  
  public NYCayugaCountyCParser() {
    super(CITY_CODES, "CAYUGA COUNTY", "NY", 
          "SRC CALL DISP ADDRCITY BOX UNIT INFO TIMEDATE! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "2083399494";
  }
  
  private static final String MARKER = "Cayuga County E911: ";
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith(MARKER)) return false;
    body = body.substring(MARKER.length()).trim();
    int pt = body.indexOf("\nText Stop");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3,4}", true);
    if (name.equals("DISP")) return new SkipField("DISP", true);
    if (name.equals("BOX")) return new BoxField("[A-Z0-9]+", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    return super.getField(name);
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d \\d\\d \\d\\d) (\\d\\d/\\d\\d/\\d{4}) - +(.*)");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1).replace(' ', ':');
      data.strDate = match.group(2);
      data.strSupp = append(data.strSupp, "\n", match.group(3));
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FH",     "FAIR HAVEN",
      "VIC",    "VICTORY",
      "WOL",    "WOLCOTT"
  });

}
