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
    setFieldList("ID CALL ADDR APT CITY UNIT X");
    this.expSubject = expSubject;
  }

  public DispatchA58Parser(String expSubject, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setFieldList("ID CALL ADDR APT CITY UNIT X");
    this.expSubject = expSubject;
  }

  private static Pattern MASTER = Pattern.compile("(?:(\\d+[-A-Z0-9]+): )?Event Notice: (.*?) at (?:\\(Location Not Entered\\)|(.*?,  .*?)|(.*?)) *(?:/FD: *(.*?)|/no unit provided|)(?:/Location not matched in geo file|/free-text location notes|/(.*?))?/(?:no call taker provided|by .*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals(expSubject)) return false;

    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    data.strCallId = getOptGroup(mat.group(1));
    data.strCall = mat.group(2).trim();

    String addr = mat.group(3);
    if (addr != null) {
      addr = addr.replace(",  ", " & ");
    } else {
      addr = mat.group(4);
    }
    if (addr != null) {
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr.trim(), data);
    }

    //UNIT CROSS
    data.strUnit = getOptGroup(mat.group(5));
    data.strCross = getOptGroup(mat.group(6));
    return true;
  }
}
