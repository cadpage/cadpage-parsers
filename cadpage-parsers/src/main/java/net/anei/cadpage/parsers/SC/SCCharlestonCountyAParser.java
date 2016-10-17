package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCCharlestonCountyAParser extends FieldProgramParser {
  
  public SCCharlestonCountyAParser() {
    super("CHARLESTON COUNTY", "SC",
           "( PREFIX Address:ADDR! X_Street:X Cmd_Channel:CH% | ADDR2/SC! X_Street:X Cmd_Channel:CH! Units_Assigned:UNIT% Time:TIME )");
  }
  
  @Override
  public String getFilter() {
    return "@charlestoncounty.org,8573031986,2183500260";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Strip off text msg heading
    if (body.startsWith("/ Dispatch Info / ")) {
      subject = "Dispatch Info";
      body = body.substring(18).trim();
    }
    
    // See if we can parse this as a fixed field message
    if (!parseFixedFieldMsg(subject, body, data)) {
      
      // No luck, try it as a variable length field message
      data.initialize(this);
      body = body.replace(" Op Channel:", " Cmd Channel:")
                 .replace(" Cmnd Channel:", " Cmd Channel:")
                 .replace(" X Streets:", " X Street:");
      if (! super.parseMsg(body, data)) return false;
    }
    if (data.strCall.length() == 0) return false;
    if (data.strAddress.length() == 0) return false;
    data.strChannel = stripFieldStart(data.strChannel, "_");
    return true;
  }

  private boolean parseFixedFieldMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Info") && !body.startsWith("CHARLESTON COUNTY: ")) return false;
    FParser p = new FParser(body);
    
    int callLen;
    if (p.check("Response Group*") || p.check("*")) callLen = 29;
    else if (p.check("CHARLESTON COUNTY: ")) callLen = 30;
    else callLen = -1;
    if (callLen > 0) {
      setFieldList("CALL ADDR APT X CH UNIT");
      data.strCall = stripFieldStart(p.get(callLen), "*");
      if (p.check(" ")) return false;
      parseAddress(p.get(40), data);
      if (p.check("X Streets:")) {
        data.strCross = p.get(40);
        p.setOptional();
        if (!p.check("Cmd Channel:")) return false;
        data.strChannel = p.get(30);
        if (!p.check("Unit Assigned:")) return false;
        data.strUnit = p.get();
      } else if (p.check("X St:")) {
        data.strCross = p.get(30);
        p.setOptional();
        if (!p.check("Cmd Chan:")) return false;
        data.strChannel = stripFieldStart(p.get(15), "_");
        if (!p.check("Units:")) return false;
        data.strUnit = p.get();
      } else if (p.check("C:")) {
        data.strChannel = stripFieldStart(p.get(15), "_");
        if (!p.check("U:")) return false;
        data.strUnit = p.get();
      } else return false;
      if (body.length() < 176) data.expectMore = true;
      return true;
    }
    
    else {
      setFieldList("UNIT ID SRC CALL CODE ADDR X APT INFO PLACE");
      data.strUnit = p.get(14);
      data.strCallId = p.get(20);
      if (!p.check("District ")) return false;
      data.strSource = p.get(3);
      data.strCall = p.get(8);
      data.strCode = p.get(10);
      parseAddress(p.get(20), data);
      if (!p.check("XS:")) return false;
      data.strCross = p.get(34);
      p.setOptional();
      if (!p.check("Apt/Bldg:")) return false;
      data.strApt = p.get(13);
      data.strSupp = p.get(30);
      if (!p.check("Location Name:")) return false;
      data.strPlace = p.get();
      if (body.length() < 130) data.expectMore = true;
      return true;
    }
  }

  private static final Pattern PREFIX_PTN = 
      Pattern.compile("(\\d{4}-\\d{7}) District (\\d{2}) (.*)");
  private class PrefixField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = PREFIX_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        data.strSource = match.group(2);
        field =  match.group(3);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID SRC CALL";
    }
  }
  
  private static final Pattern RESP_AREA_PTN = 
      Pattern.compile("^(NCFD (?:NORTH|SOUTH|EAST|WEST) \\d+|[A-Z]{2}FD \\d+|FD WEST ASHLEY RIVER|CHFD \\d+|(?:ST|TH) NAVAL WEAPONS STA)\\b");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Response Area: ");
      if (pt >= 0) {
        String call = field.substring(0,pt).trim();
        field = field.substring(pt+16).trim();
        Matcher match = RESP_AREA_PTN.matcher(field);
        if (match.find()) {
          data.strMap = match.group(1);
          parseAddress(field.substring(match.end()).trim(), data);
        } else {
          super.parse(field, data);
          data.strMap = data.strCall;
        }
        data.strCall = call;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL MAP ADDR APT";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new PrefixField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    return super.getField(name);
  }
}
