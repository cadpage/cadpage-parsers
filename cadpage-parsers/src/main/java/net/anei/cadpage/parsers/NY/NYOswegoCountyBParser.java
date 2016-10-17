package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;

public class NYOswegoCountyBParser extends DispatchH01Parser {
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([^ ]+) - +(.*)");
  
  public NYOswegoCountyBParser() {
    super(CITY_CODES, "OSWEGO COUNTY", "NY",
          "MARK! Location:ADDR! Response_Type:CALL! Zone_Name:MAP! Creation_Time:SKIP! Status_Name:SKIP! Status_Time:DATETIME! Handling_Unit:UNIT! Agency_Name:SRC! Case_Number:ID! NOTES+");
  }
  
  @Override
  public String getFilter() {
    return "911@OSWEGOCOUNTY.COM";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("FIRE DISPATCH REPORT", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLE", "CLEVELAND",
      "CON", "CONSTANTIA",
      "CSQ", "CENTRAL SQUARE",
      "HAS", "HASTINGS",
      "PAR", "PARISH",
      "SCH", "SCHROEPPEL",
      "WMN", "WEST MONROE",
      "WMR", "WEST MONROE"
  });
}
	