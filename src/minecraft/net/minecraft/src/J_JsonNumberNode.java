// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

// Referenced classes of package net.minecraft.src:
//            J_JsonNode, EnumJsonNodeType

final class J_JsonNumberNode extends J_JsonNode {

	private static final Pattern field_27226_a = Pattern.compile("(-?)(0|([1-9]([0-9]*)))(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
	private final String field_27225_b;

	J_JsonNumberNode(String s) {
		if(s == null) {
			throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
		}
		if(!field_27226_a.matcher(s).matches()) {
			throw new IllegalArgumentException((new StringBuilder()).append("Attempt to construct a JsonNumber with a String [").append(s).append("] that does not match the JSON number specification.").toString());
		} else {
			field_27225_b = s;
			return;
		}
	}

	@Override
	public EnumJsonNodeType getType() {
		return EnumJsonNodeType.NUMBER;
	}

	@Override
	public String getText() {
		return field_27225_b;
	}

	@Override
	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	@Override
	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		} else {
			J_JsonNumberNode j_jsonnumbernode = (J_JsonNumberNode) obj;
			return field_27225_b.equals(j_jsonnumbernode.field_27225_b);
		}
	}

	@Override
	public int hashCode() {
		return field_27225_b.hashCode();
	}

	@Override
	public String toString() {
		return (new StringBuilder()).append("JsonNumberNode value:[").append(field_27225_b).append("]").toString();
	}

}
