package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXWichitaCountyBParser extends FieldProgramParser {

  public TXWichitaCountyBParser() {
    super("WICHITA COUNTY", "TX",
          "CALL PLACE ADDR CH! END");
  }

  @Override
  public String getFilter() {
    return "PublicSafety@WichitaFallsTX.gov";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ,]+(?=\\[\\d{1,2}\\])");
  private static final Pattern INFO_EMPTY_PTN = Pattern.compile("\\[\\d}1,2}\\]");
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*Additional Information:.*; LAT (\\S+) ; LON (\\S+) ;.*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Enterprise CAD System Page")) return false;
    String info = null;
    int pt = body.indexOf(" Comments:");
    if (pt >= 0) {
      info = body.substring(pt+10).trim();
      body = body.substring(0,pt).trim();
    }

    if (!parseFields(body.split(":"), data)) return false;
    if (info != null) {
      for (String line : INFO_BRK_PTN.split(stripFieldEnd(info, ","))) {
        if (INFO_EMPTY_PTN.matcher(line).matches()) continue;
        Matcher match = INFO_GPS_PTN.matcher(line);
        if (match.matches()) {
          setGPSLoc(match.group(1)+','+match.group(2), data);
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " GPS INFO";
  }
}
