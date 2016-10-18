package net.anei.cadpage.parsers.NC;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCWilsonCountyParser extends DispatchOSSIParser {
  
  public NCWilsonCountyParser() {
    super("WILSON COUNTY", "NC",
           "ID? CODE? CALL ADDR! ( PLACE SRC | SRC? )  X+? INFO+");
  }

  @Override
  public String getFilter() {
    return "@Wilson-co.com,930010";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    body = stripFieldStart(body, "/ Text Message / ");
    
    if (subject.equals("Sanoca")) {
      if (body.startsWith(";")) body = "CAD:" + body.substring(1).trim();
    }
    else if (body.startsWith("CAD:") && subject.contains(";")) {
      body = "CAD:" + subject + ' ' + body.substring(4); 
    }
    return super.parseMsg(body,  data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6,}", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Za-z]\\d\\d[A-Za-z]?");
    if (name.equals("SRC")) return new SourceField("\\d{1,2}[A-Z0-9]|[A-Z]\\d|NAS", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (US_264_DIR.matcher(field).matches()) {
        parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  
  @Override
  public String adjustMapAddress(String address) {
    Matcher match = US_264_DIR.matcher(address);
    if (match.find()) {
      StringBuffer bf = new StringBuffer();
      do {
        match.appendReplacement(bf, "US 264");
        if (match.group(1) != null) bf.append(" ALT");
      } while (match.find());
      match.appendTail(bf);
      address = bf.toString();
    }
    return address;
  }
  private static final Pattern US_264_DIR = Pattern.compile("\\bUS +264(A?)(?: +[NESW])?\\b", Pattern.CASE_INSENSITIVE);
}