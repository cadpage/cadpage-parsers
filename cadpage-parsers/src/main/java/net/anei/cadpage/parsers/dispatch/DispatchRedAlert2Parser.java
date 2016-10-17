package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchRedAlert2Parser extends FieldProgramParser {
  
  private static final Pattern CODE_PTN = Pattern.compile("^(\\d{1,2}-[A-Z]-\\d{1,2}[A-Z]?) ");
  
  private boolean checkCity;
  
  public DispatchRedAlert2Parser(String defCity,  String defState) {
    this(null, defCity, defState);
  }
  
  public DispatchRedAlert2Parser(String[] cityList, String defCity,  String defState) {
    super(cityList, defCity, defState,
          "CALL! code:CODE? at:ADDR! c/s:X! d/t:DATETIME box:BOX Inc#:ID");
    checkCity = (cityList != null);
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCode.length() == 0) {
      Matcher match = CODE_PTN.matcher(data.strCall);
      if (match.find()) {
        data.strCode = match.group(1);
        data.strCall = data.strCall.substring(match.end()).trim();
      }
    }
    
    if (data.strAddress.length() == 0) {
      String sAddr = data.strCross;
      data.strCross = "";
      parseAddress(sAddr, data);
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressCityField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }
  
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (!checkCity) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("&")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }
}
