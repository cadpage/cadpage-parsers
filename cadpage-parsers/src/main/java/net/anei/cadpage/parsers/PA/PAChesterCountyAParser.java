package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyAParser extends PAChesterCountyBaseParser {
  
  private static final Pattern IAR_PTN = Pattern.compile("^[A-Z]+ +Final Type:");
  
  public PAChesterCountyAParser() {
    super("EMPTY Initial_Type:SKIP! Final_Type:CALL! Loc:ADDRCITY! btwn:X? AKA:INFO");
  }
  
  @Override
  public String getFilter() {
    return "gallison39@comcast.net,messaging@iamresponding.com";
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strSource = subject;
    
    // Fix up IAmResponding modifications :(
    if (IAR_PTN.matcher(body).find()) body = "Initial Type:" + body;

    // Replace key chars for easier parsing
    body = body.replace("\n"," ");
    body = body.replace(" btwn "," btwn:");
    body = body.replace("(V)", "");
    body = body.replaceAll("  +", " ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private class MyAddressCityField extends BaseAddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" -- ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+4).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return AL_PTN.matcher(addr).replaceAll("ALLEY");
  }
  private static final Pattern AL_PTN = Pattern.compile("\\bAL\\b", Pattern.CASE_INSENSITIVE);
} 
