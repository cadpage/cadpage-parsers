package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class MOFranklinCountyParser extends DispatchH01Parser {
  public MOFranklinCountyParser() {
    super("FRANKLIN COUNTY", "MO",
          "( MARK1! Workstation:SKIP! Print_Time:SKIP! User:SKIP! Location:ADDR! Response_Type:CALL! Zone_Name:MAP! Status_Name:SKIP! Status_Time:DATETIME! Handling_Unit:UNIT! Agency:SRC! NOTES+ " +
          "| MARK2/R! Number:ID? Location:ADDR! Response_Type:CALL! Agency:SRC! HandlingResource:UNIT! CallerName:NAME? PhoneNumber:PHONE? ZoneName:MAP! Response_State_Logs%EMPTY RR_NOTES/G+? END_TABLE Resource_Activities%EMPTY RR_NOTES+? END_TABLE " +
          "| EMPTY CALL ADDR! END )");
  }
  
  @Override
  public String getFilter() {
    return "@franklinmo.net,@sbcglobal.net";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("[-.,@A-Za-z0-9]+\n{2,}");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();
    
    int pt = body.indexOf("Scanned by YHTI SPAM firewall");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if (body.startsWith("<HTML><body>")) {
      body = decodeHtmlSequence(body);
      return parseFields(body.split("\n"), data);
    }
    
    if (! super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equals("FRNKLN CNTY")) data.strCity = "FRANKLIN COUNTY";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK1")) return new SkipField("Franklin Co Response Email Report", true);
    if (name.equals("MARK2")) return new SkipField("Franklin Co Completed Response Report", true);
    return super.getField(name);
  }
}