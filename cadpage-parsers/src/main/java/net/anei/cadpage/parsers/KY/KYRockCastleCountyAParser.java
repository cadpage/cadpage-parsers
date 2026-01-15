package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class KYRockCastleCountyAParser extends DispatchA48Parser {

  public KYRockCastleCountyAParser() {
    super(CITY_LIST, "ROCKCASTLE COUNTY", "KY", FieldType.X_NAME, A48_NO_FLD_BREAKS);
  }

  @Override
  public String getFilter() {
    return "Rock911@windstream.net";
  }

  private static final String[] CITY_LIST = new String[]{
      "BRODHEAD",
      "LIVINGSTON",
      "MOUNT VERNON"
  };
}
