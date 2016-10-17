package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WICalumetCountyBParser extends FieldProgramParser {
  
  public WICalumetCountyBParser() {
    super("CALUMET COUNTY", "WI",
          "ADDR PLACE APT? CITY CALL CALL+? ( NONE ID! TIME | ID! ( EMPTY NONE! TIME | ) ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "zoll@goldcross.org,donotreply@goldcross.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Gold Cross Alert")) return false;
    if (!body.startsWith("RC:")) return false;
    body = body.substring(3).trim();
    return parseFields(body.split("/"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("\\d{1,4}[A-Z]?|[A-Z]", true);
    if (name.equals("NONE")) return new SkipField("<None>", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Run# ")) {
        super.parse(field.substring(5).trim(), data);
        return true;
      } else {
        return (field.length() > 0 && "Run# ".startsWith(field));
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
//  
//  @Override
//  public String adjustMapAddress(String sAddress) {
//    if (W_DIGIT_PTN.matcher(sAddress).find()) sAddress = sAddress.substring(1);
//    return sAddress;
//  }
//  private static final Pattern W_DIGIT_PTN = Pattern.compile("^W\\d+ ");
}