package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NVClarkCountyEParser extends MsgParser {
  
  public NVClarkCountyEParser() {
    super("CLARK COUNTY", "NV");
  }
  
  @Override
  public String getFilter() {
    return "countydsp@clarkcountynv.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD")) return false;
    
    FParser fp = new FParser(body);
    if (fp.checkAhead(10, "INC  #")) {
      setFieldList("ID CODE CALL ADDR APT MAP");
      fp.skip(16);
      data.strCallId = fp.get(16);
      if (!fp.check("  PROBLEM")) return false;
      parseCall(fp.get(30), data);
      if (!fp.check("ADDR")) return false;
      parseAddress(fp.get(40), data);
      if (!fp.check("BatBat ")) return false;
      data.strMap = fp.get(2);
      if (!fp.check("  DivDiv ")) return false;
      data.strMap = append(data.strMap, "-", fp.get());
      return true;
    }
    setFieldList("ADDR APT X CODE CALL PRI MAP ID UNIT INFO");
    parseAddress(fp.get(30), data);
    if (!fp.check("X Street")) return false;
    data.strCross = fp.get(60);
    parseCall(fp.get(30), data);
    if (!fp.check("Pri:")) return false;
    data.strPriority = fp.get(30);
    if (!fp.check("Dist/Phan")) return false;
    data.strMap = append(data.strMap, "/", fp.get(10));
    if (!fp.check("Inc #")) return false;
    data.strCallId = fp.get(19);
    fp.setOptional();
    if (!fp.check(" ")) return false;
    
    // Rest is free form and mostly junk
    parseExtra(fp.get(), data);
    return true;
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- *(.*)");
  private void parseCall(String field, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(field);
    if (match.matches()) {
      data.strCode = match.group(1);
      field = match.group(2);
    }
    data.strCall = field;
  }
  
  private static final Pattern EXTRA_UNIT_PTN = Pattern.compile("Units;(.*?)comments;");
  private void parseExtra(String field, Data data) {
    Matcher match = EXTRA_UNIT_PTN.matcher(field);
    if (match.lookingAt()) {
      data.strUnit = match.group(1).trim();
      field = field.substring(match.end()).trim();
    }
    else {
      field = stripFieldStart(field, "Comments");
    }
    
    // Throw away anything between square brackets, and process
    // anything outside of them
    int last = 0;
    for (int pt = 0; pt<field.length(); pt++) {
      int chr = field.charAt(pt);
      if (last>=0 && chr ==  '[') {
        parseExtraField(field.substring(last, pt), data);
        last = -1;
      }
      else if (last < 0 && chr == ']') {
        last = pt+1;
      }
    }
    if (last >= 0) {
      parseExtraField(field.substring(last), data);
    }
  }
  
  private static final Pattern EXTRA_FLD_PTN = Pattern.compile("FZ[A-Z0-9]+, *(.*)");
  private void parseExtraField(String field, Data data) {
    field = field.trim();
    if (field.length() == 0) return;
    if (field.startsWith("Class of service:")) return;
    Matcher match = EXTRA_FLD_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    data.strSupp = append(data.strSupp, "\n", field);
  }
}