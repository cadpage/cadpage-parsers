package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


public class TXNuecesCountyParser extends FieldProgramParser {
  
  public TXNuecesCountyParser() {
    super("NUECES COUNTY", "TX",
           "CALL! ALRM:SKIP! PRI:PRI! ESZ:UNIT! TIME:TIME? EV:ID");
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 150; }
      @Override public int splitBreakPad() { return 1; }
    };
  }


  @Override
  public String getFilter() {
    return "ccpdpaging@cctexas.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern SRC_ID_PTN = Pattern.compile("([ A-Z]+)[: ] +(\\d{10}) +");
 
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("CAD Notify")) break;
      
      if (body.startsWith("CAD Notify ")) {
        body = body.substring(11).trim();
        break;
      }
      return false;
    } while (false);
    
    Matcher match = SRC_ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1).trim();
      data.strCallId = match.group(2);
      body = body.substring(match.end());
    }
    
    body = body.replace("TIME:", " TIME:");
    if (!super.parseMsg(body, data)) return false;
    return data.strAddress.length() > 0;
  }
  
  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:(\\d+)|-1)(?:[ /]+(.*))?");
  private class MyUnitField extends MyAddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = getOptGroup(match.group(1));
      String addr = match.group(2);
      if (addr != null) super.parse(addr, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames();
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) +(.*)");
  private class MyTimeField extends MyAddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      super.parse(match.group(2), data);
    }
    
    @Override
    public String getFieldNames() {
      return "TIME " + super.getFieldNames();
    }
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*)[,:;] *([-A-Z0-9]+)");
  private abstract class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim().replace(": @", " - ");
        field = field.substring(0,pt);
      }
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      field = stripFieldEnd(field, " NUECS");
      if (field.endsWith(" CC")) {
        data.strCity = "CORPUS CHRISTI";
        field = field.substring(0, field.length()-3).trim();
      }
      String alias = null;
      pt = field.indexOf(": alias ");
      if (pt >= 0) {
        alias = field.substring(pt+8).trim();
        field = field.substring(0,pt).trim();
        field = stripFieldEnd(field, " NUECS");
        if (field.endsWith(" CC")) {
          data.strCity = "CORPUS CHRISTI";
          field = field.substring(0, field.length()-3).trim();
        }
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
      if (alias != null) {
        data.strAddress = append(data.strAddress, " ", '(' + alias + ')');
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
}
