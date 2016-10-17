package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA58Parser extends SmartAddressParser {
  
  private String expSubject;

  public DispatchA58Parser(String expSubject, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setFieldList("CALL ADDR APT CITY UNIT X");
    this.expSubject = expSubject;
  }

  public DispatchA58Parser(String expSubject, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setFieldList("CALL ADDR APT CITY UNIT X");
    this.expSubject = expSubject;
  }

  private static Pattern MASTER = Pattern.compile("Event Notice: (.*?) at (?:\\(Location Not Entered\\)|(.*?,  .*?)|(.*?)) *(?:/FD: *(.*?))?(?:/Location not matched in geo file|/(.*?))?/by .*");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals(expSubject)) return false;
    
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    
    //CALL
    data.strCall = mat.group(1).trim();
    
    //ADDR
    String addr = mat.group(2);
    if (addr != null) {
      addr = addr.replace(",  ", " & ");
    } else {
      addr = mat.group(3);
    }
    if (addr != null) {
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr.trim(), data);
    }
    
    //UNIT CROSS
    data.strUnit = getOptGroup(mat.group(4));
    data.strCross = getOptGroup(mat.group(5));
    return true;
  }
}
