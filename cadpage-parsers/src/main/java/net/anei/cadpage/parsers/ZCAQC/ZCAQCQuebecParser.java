package net.anei.cadpage.parsers.ZCAQC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Quebec, QC, CA
 */
public class ZCAQCQuebecParser extends FieldProgramParser {
  
  // We have to use the /S address parser because that is the easiers way
  // to get it to ignore the STE keyword which is usually an abbreviation for
  // suite.  But in French is a commonly used street abbreviation.
  public ZCAQCQuebecParser() {
    super("QUEBEC", "QC",
          "CALL ADDR/S CITY!");
    removeWords("STE");
  }

  @Override
  public String getFilter() {
    return "Jacques.Lachance@ville.quebec.qc.ca";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("test")) return false;
    return super.parseFields(body.split(";"), 3, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = RU_PTN.matcher(addr).replaceAll("RUE");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern RU_PTN = Pattern.compile("\\bRU\\b", Pattern.CASE_INSENSITIVE);
}
