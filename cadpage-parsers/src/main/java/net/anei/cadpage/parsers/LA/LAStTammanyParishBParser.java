package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class LAStTammanyParishBParser extends DispatchA46Parser {

  public LAStTammanyParishBParser() {
    super("ST TAMMANY PARISH", "LA");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "dispatch@covla.com";
  }
  
  private static final Pattern INN_PTN = Pattern.compile("\\bI[- ]*(\\d+) *([EW])B?\\b");
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return INN_PTN.matcher(address.toUpperCase()).replaceAll("I-$1$2");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "53 I-12E", "30.479669,-90.244428",
      "53 I-12W", "30.480402,-90.244345",
      "54 I-12E", "30.474260,-90.226186",
      "54 I-12W", "30.475391,-90.226813",
      "55 I-12E", "30.467916,-90.212069",
      "55 I-12W", "30.468554,-90.211725",
      "56 I-12E", "30.462656,-90.195502",
      "56 I-12W", "30.463024,-90.195342",
      "57 I-12E", "30.458226,-90.180528",
      "57 I-12W", "30.458648,-90.180360",
      "58 I-12E", "30.453720,-90.164768",
      "58 I-12W", "30.454071,-90.164606",
      "59 I-12E", "30.449008,-90.148600",
      "59 I-12W", "30.449353,-90.148382",
      "60 I-12E", "30.443965,-90.131883"

  });
}
