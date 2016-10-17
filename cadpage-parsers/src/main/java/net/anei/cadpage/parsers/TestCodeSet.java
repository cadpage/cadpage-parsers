package net.anei.cadpage.parsers;

import java.util.Set;

/**
 * This implements a dummy CodeSet object backed by a Set<String> object.  It does
 * not pretend  to implement full CodeSet functionality, but it implements enough
 * that it returned by a MsgParser.getCallList() method to support call description
 * verification for parsers that use a Set based call description lookup
 */
public class TestCodeSet extends CodeSet {

  private Set<String> codeSet;
  
  public TestCodeSet(Set<String> codeSet) {
    this.codeSet =  codeSet;
  }

  @Override
  protected void add(String code) {
    codeSet.add(code);
  }

  @Override
  public String getCode(String code, boolean reqSpace) {
    return codeSet.contains(code) ? code : null;
  }
  
}
