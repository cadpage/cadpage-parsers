package net.anei.cadpage.parsers.ZUK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ZUKWestMidlandsParser extends SmartAddressParser {

  private static final Pattern MASTER =
    Pattern.compile("(E:\\d+) +\\((\\d\\d:\\d\\d)\\) *(.*?) ([A-Z]{1,2}\\d{1,2}[A-Z]? \\d[A-Z]{2}) +(.*?) +\\((\\d+,\\d+)\\)");

  public ZUKWestMidlandsParser() {
    super(CITY_LIST, "", "West Midlands", CountryCode.UK);
    setFieldList("ID TIME CALL PLACE ADDR CITY MAP");
  }
  
  @Override
  public String getFilter() {
    return "447624805974";
  }

  @Override
  public String getLocName() {
    return "West Midlands Region, UK";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    data.strCallId = match.group(1);
    data.strTime = match.group(2);
    data.strCall = match.group(3);
    String sCode = match.group(4);
    String sAddr = match.group(5);
    data.strMap = match.group(6);
    parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, sAddr, data);
    if (data.strAddress.length() == 0) {
      data.strAddress = data.strPlace;
      data.strPlace = "";
    }
    data.strAddress = sCode + " " + data.strAddress;
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    "BIRMINGHAM",
    "BURTON-UPON-TRENT",
    "CANNOCK",
    "COVENTRY",
    "DUDLEY",
    "HALESOWEN",
    "HEREFORD",
    "KIDDERMINSTER",
    "KINGSWINFORD",
    "LEAMINGTON SPA",
    "LEEK",
    "LICHFIELD",
    "NEWCASTLE-UNDER-LYME",
    "NUNEATON",
    "OTHER NOTABLE",
    "REDDITCH",
    "RUGBY",
    "SHREWSBURY",
    "SOLIHULL",
    "STAFFORD",
    "STOKE-ON-TRENT",
    "STOURBRIDGE",
          "AMBLECOTE STOURBRIDGE",
          "HAGLEY STOURBRIDGE",
          "NORTON STOURBRIDGE",
          "WOLLESCOTE STOURBRIDGE",
          "WORDSLEY STOURBRIDGE",
    "STRATFORD-UPON-AVON",
    "SUTTON COLDFIELD",
    "TAMWORTH",
    "TELFORD",
    "WALSALL",
    "WARWICK",
    "WEST BROMWICH",
    "WOLVERHAMPTON",
    "WORCESTER"
  };
}
