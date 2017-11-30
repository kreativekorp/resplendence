package com.kreative.resplendence.template;

import com.kreative.ksfl.KSFLUtilities;

public class TemplateField {
	public static final int TYPE_DEC_NIBBLE_BE = 0x444E4942;
	public static final int TYPE_DEC_BYTE_BE = 0x44425954;
	public static final int TYPE_DEC_WORD_BE = 0x44575244;
	public static final int TYPE_DEC_LONG_BE = 0x444C4E47;
	public static final int TYPE_DEC_LONG_LONG_BE = 0x444C4C47;
	public static final int TYPE_DEC_SINT4_BE = 0x44533034;
	public static final int TYPE_DEC_SINT8_BE = 0x44533038;
	public static final int TYPE_DEC_SINT16_BE = 0x44533136;
	public static final int TYPE_DEC_SINT24_BE = 0x44533234;
	public static final int TYPE_DEC_SINT32_BE = 0x44533332;
	public static final int TYPE_DEC_SINT48_BE = 0x44533438;
	public static final int TYPE_DEC_SINT64_BE = 0x44533634;
	public static final int TYPE_DEC_UINT4_BE = 0x44553034;
	public static final int TYPE_DEC_UINT8_BE = 0x44553038;
	public static final int TYPE_DEC_UINT16_BE = 0x44553136;
	public static final int TYPE_DEC_UINT24_BE = 0x44553234;
	public static final int TYPE_DEC_UINT32_BE = 0x44553332;
	public static final int TYPE_DEC_UINT48_BE = 0x44553438;
	public static final int TYPE_DEC_UINT64_BE = 0x44553634;

	public static final int TYPE_HEX_NIBBLE_BE = 0x484E4942;
	public static final int TYPE_HEX_BYTE_BE = 0x48425954;
	public static final int TYPE_HEX_WORD_BE = 0x48575244;
	public static final int TYPE_HEX_LONG_BE = 0x484C4E47;
	public static final int TYPE_HEX_LONG_LONG_BE = 0x484C4C47;
	public static final int TYPE_HEX_SINT4_BE = 0x48533034;
	public static final int TYPE_HEX_SINT8_BE = 0x48533038;
	public static final int TYPE_HEX_SINT16_BE = 0x48533136;
	public static final int TYPE_HEX_SINT24_BE = 0x48533234;
	public static final int TYPE_HEX_SINT32_BE = 0x48533332;
	public static final int TYPE_HEX_SINT48_BE = 0x48533438;
	public static final int TYPE_HEX_SINT64_BE = 0x48533634;
	public static final int TYPE_HEX_UINT4_BE = 0x48553034;
	public static final int TYPE_HEX_UINT8_BE = 0x48553038;
	public static final int TYPE_HEX_UINT16_BE = 0x48553136;
	public static final int TYPE_HEX_UINT24_BE = 0x48553234;
	public static final int TYPE_HEX_UINT32_BE = 0x48553332;
	public static final int TYPE_HEX_UINT48_BE = 0x48553438;
	public static final int TYPE_HEX_UINT64_BE = 0x48553634;

	public static final int TYPE_OCT_NIBBLE_BE = 0x4F4E4942;
	public static final int TYPE_OCT_BYTE_BE = 0x4F425954;
	public static final int TYPE_OCT_WORD_BE = 0x4F575244;
	public static final int TYPE_OCT_LONG_BE = 0x4F4C4E47;
	public static final int TYPE_OCT_LONG_LONG_BE = 0x4F4C4C47;
	public static final int TYPE_OCT_SINT4_BE = 0x4F533034;
	public static final int TYPE_OCT_SINT8_BE = 0x4F533038;
	public static final int TYPE_OCT_SINT16_BE = 0x4F533136;
	public static final int TYPE_OCT_SINT24_BE = 0x4F533234;
	public static final int TYPE_OCT_SINT32_BE = 0x4F533332;
	public static final int TYPE_OCT_SINT48_BE = 0x4F533438;
	public static final int TYPE_OCT_SINT64_BE = 0x4F533634;
	public static final int TYPE_OCT_UINT4_BE = 0x4F553034;
	public static final int TYPE_OCT_UINT8_BE = 0x4F553038;
	public static final int TYPE_OCT_UINT16_BE = 0x4F553136;
	public static final int TYPE_OCT_UINT24_BE = 0x4F553234;
	public static final int TYPE_OCT_UINT32_BE = 0x4F553332;
	public static final int TYPE_OCT_UINT48_BE = 0x4F553438;
	public static final int TYPE_OCT_UINT64_BE = 0x4F553634;

	public static final int TYPE_BIN_NIBBLE_BE = 0x424E4942;
	public static final int TYPE_BIN_BYTE_BE = 0x42425954;
	public static final int TYPE_BIN_WORD_BE = 0x42575244;
	public static final int TYPE_BIN_LONG_BE = 0x424C4E47;
	public static final int TYPE_BIN_LONG_LONG_BE = 0x424C4C47;
	public static final int TYPE_BIN_SINT4_BE = 0x42533034;
	public static final int TYPE_BIN_SINT8_BE = 0x42533038;
	public static final int TYPE_BIN_SINT16_BE = 0x42533136;
	public static final int TYPE_BIN_SINT24_BE = 0x42533234;
	public static final int TYPE_BIN_SINT32_BE = 0x42533332;
	public static final int TYPE_BIN_SINT48_BE = 0x42533438;
	public static final int TYPE_BIN_SINT64_BE = 0x42533634;
	public static final int TYPE_BIN_UINT4_BE = 0x42553034;
	public static final int TYPE_BIN_UINT8_BE = 0x42553038;
	public static final int TYPE_BIN_UINT16_BE = 0x42553136;
	public static final int TYPE_BIN_UINT24_BE = 0x42553234;
	public static final int TYPE_BIN_UINT32_BE = 0x42553332;
	public static final int TYPE_BIN_UINT48_BE = 0x42553438;
	public static final int TYPE_BIN_UINT64_BE = 0x42553634;

	public static final int TYPE_ALIGN_NIBBLE_BE = 0x414E4942;
	public static final int TYPE_ALIGN_BYTE_BE = 0x41425954;
	public static final int TYPE_ALIGN_WORD_BE = 0x41575244;
	public static final int TYPE_ALIGN_LONG_BE = 0x414C4E47;
	public static final int TYPE_ALIGN_LONG_LONG_BE = 0x414C4C47;
	public static final int TYPE_ALIGN4_BE = 0x414C3034;
	public static final int TYPE_ALIGN8_BE = 0x414C3038;
	public static final int TYPE_ALIGN16_BE = 0x414C3136;
	public static final int TYPE_ALIGN24_BE = 0x414C3234;
	public static final int TYPE_ALIGN32_BE = 0x414C3332;
	public static final int TYPE_ALIGN48_BE = 0x414C3438;
	public static final int TYPE_ALIGN64_BE = 0x414C3634;
	
	public static final int TYPE_FILLER_BIT_BE = 0x46424954;
	public static final int TYPE_FILLER_NIBBLE_BE = 0x464E4942;
	public static final int TYPE_FILLER_BYTE_BE = 0x46425954;
	public static final int TYPE_FILLER_WORD_BE = 0x46575244;
	public static final int TYPE_FILLER_LONG_BE = 0x464C4E47;
	public static final int TYPE_FILLER_LONG_LONG_BE = 0x464C4C47;
	public static final int TYPE_FILLER1_BE = 0x464C3031;
	public static final int TYPE_FILLER4_BE = 0x464C3034;
	public static final int TYPE_FILLER8_BE = 0x464C3038;
	public static final int TYPE_FILLER16_BE = 0x464C3136;
	public static final int TYPE_FILLER24_BE = 0x464C3234;
	public static final int TYPE_FILLER32_BE = 0x464C3332;
	public static final int TYPE_FILLER48_BE = 0x464C3438;
	public static final int TYPE_FILLER64_BE = 0x464C3634;
	
	public static final int TYPE_BOOLEAN_BIT_BE = 0x42424954;
	public static final int TYPE_BOOLEAN_WORD_BE = 0x424F4F4C;
	public static final int TYPE_BOOLEAN1_BE = 0x424C3031;
	public static final int TYPE_BOOLEAN4_BE = 0x424C3034;
	public static final int TYPE_BOOLEAN8_BE = 0x424C3038;
	public static final int TYPE_BOOLEAN16_BE = 0x424C3136;
	public static final int TYPE_BOOLEAN24_BE = 0x424C3234;
	public static final int TYPE_BOOLEAN32_BE = 0x424C3332;
	public static final int TYPE_BOOLEAN48_BE = 0x424C3438;
	public static final int TYPE_BOOLEAN64_BE = 0x424C3634;

	public static final int TYPE_FIXED4_BE = 0x46583034;
	public static final int TYPE_FIXED8_BE = 0x46583038;
	public static final int TYPE_FIXED16_BE = 0x46583136;
	public static final int TYPE_FIXED24_BE = 0x46583234;
	public static final int TYPE_FIXED32_BE = 0x46583332;
	public static final int TYPE_FIXED48_BE = 0x46583438;
	public static final int TYPE_FIXED64_BE = 0x46583634;
	
	public static final int TYPE_FLOAT_QUARTER_BE = 0x51554152;
	public static final int TYPE_FLOAT_HALF_BE = 0x48414C46;
	public static final int TYPE_FLOAT_3QUARTER_BE = 0x33515452;
	public static final int TYPE_FLOAT_SINGLE_BE = 0x53494E47;
	public static final int TYPE_FLOAT_DOUBLE_BE = 0x444F5542;
	
	public static final int TYPE_ENUM_BIT_BE = 0x45424954;
	public static final int TYPE_ENUM_NIBBLE_BE = 0x454E4942;
	public static final int TYPE_ENUM_BYTE_BE = 0x45425954;
	public static final int TYPE_ENUM_WORD_BE = 0x45575244;
	public static final int TYPE_ENUM_LONG_BE = 0x454C4E47;
	public static final int TYPE_ENUM_LONG_LONG_BE = 0x454C4C47;
	public static final int TYPE_ENUM1_BE = 0x454E3031;
	public static final int TYPE_ENUM4_BE = 0x454E3034;
	public static final int TYPE_ENUM8_BE = 0x454E3038;
	public static final int TYPE_ENUM16_BE = 0x454E3136;
	public static final int TYPE_ENUM24_BE = 0x454E3234;
	public static final int TYPE_ENUM32_BE = 0x454E3332;
	public static final int TYPE_ENUM48_BE = 0x454E3438;
	public static final int TYPE_ENUM64_BE = 0x454E3634;
	public static final int TYPE_ENUM_OPTION_BE = 0x454F5054;
	
	public static final int TYPE_SYMBOL_CHAR_BE = 0x43484152;
	public static final int TYPE_SYMBOL_TYPENAME_BE = 0x544E414D;
	public static final int TYPE_SYMBOL_SYMBOL_BE = 0x53594D42;
	public static final int TYPE_SYMBOL8_BE = 0x43483038;
	public static final int TYPE_SYMBOL16_BE = 0x43483136;
	public static final int TYPE_SYMBOL24_BE = 0x43483234;
	public static final int TYPE_SYMBOL32_BE = 0x43483332;
	public static final int TYPE_SYMBOL48_BE = 0x43483438;
	public static final int TYPE_SYMBOL64_BE = 0x43483634;
	
	public static final int TYPE_STRING_PASCAL8_BE = 0x50535452;
	public static final int TYPE_STRING_PASCAL16_BE = 0x57535452;
	public static final int TYPE_STRING_PASCAL24_BE = 0x4D535452;
	public static final int TYPE_STRING_PASCAL32_BE = 0x4C535452;
	public static final int TYPE_STRING_PASCAL48_BE = 0x36535452;
	public static final int TYPE_STRING_PASCAL64_BE = 0x38535452;
	public static final int TYPE_STRING_PASCAL8_EVEN_BE = 0x45535452;
	public static final int TYPE_STRING_PASCAL8_ODD_BE = 0x4F535452;
	public static final int TYPE_STRING_PASCAL8_FIXED_BE = 0x50000000;
	public static final int TYPE_STRING_C_BE = 0x43535452;
	public static final int TYPE_STRING_C_EVEN_BE = 0x45435354;
	public static final int TYPE_STRING_C_ODD_BE = 0x4F435354;
	public static final int TYPE_STRING_C_FIXED_BE = 0x43000000;
	
	public static final int TYPE_HEX_DUMP_BE = 0x48455844;
	public static final int TYPE_HEX_FIXED_BE = 0x48000000;
	
	public static final int TYPE_COORDINATE_2D_BE = 0x32445054;
	public static final int TYPE_COORDINATE_3D_BE = 0x33445054;
	public static final int TYPE_RECTANGLE_BE = 0x52454354;
	
	public static final int TYPE_COLOR_RGB555_BE = 0x52353535;
	public static final int TYPE_COLOR_RGB565_BE = 0x52353635;
	public static final int TYPE_COLOR_RGB444_BE = 0x52343434;
	public static final int TYPE_COLOR_RGB888_BE = 0x52383838;
	public static final int TYPE_COLOR_RGB161616_BE = 0x52313658;
	public static final int TYPE_COLOR_BGR555_BE = 0x35355235;
	public static final int TYPE_COLOR_BGR565_BE = 0x35365235;
	public static final int TYPE_COLOR_BGR444_BE = 0x34345234;
	public static final int TYPE_COLOR_BGR888_BE = 0x38385238;
	public static final int TYPE_COLOR_BGR161616_BE = 0x58523136;
	
	public static final int TYPE_LISTCOUNTER_ZEROBASED_BE = 0x5A434E54;
	public static final int TYPE_LISTCOUNTER4_ZEROBASED_BE = 0x5A433034;
	public static final int TYPE_LISTCOUNTER8_ZEROBASED_BE = 0x5A433038;
	public static final int TYPE_LISTCOUNTER16_ZEROBASED_BE = 0x5A433136;
	public static final int TYPE_LISTCOUNTER24_ZEROBASED_BE = 0x5A433234;
	public static final int TYPE_LISTCOUNTER32_ZEROBASED_BE = 0x5A433332;
	public static final int TYPE_LISTCOUNTER48_ZEROBASED_BE = 0x5A433438;
	public static final int TYPE_LISTCOUNTER64_ZEROBASED_BE = 0x5A433634;
	
	public static final int TYPE_LISTCOUNTER_ONEBASED_BE = 0x4F434E54;
	public static final int TYPE_LISTCOUNTER4_ONEBASED_BE = 0x4F433034;
	public static final int TYPE_LISTCOUNTER8_ONEBASED_BE = 0x4F433038;
	public static final int TYPE_LISTCOUNTER16_ONEBASED_BE = 0x4F433136;
	public static final int TYPE_LISTCOUNTER24_ONEBASED_BE = 0x4F433234;
	public static final int TYPE_LISTCOUNTER32_ONEBASED_BE = 0x4F433332;
	public static final int TYPE_LISTCOUNTER48_ONEBASED_BE = 0x4F433438;
	public static final int TYPE_LISTCOUNTER64_ONEBASED_BE = 0x4F433634;
	
	public static final int TYPE_LIST_EOF_TERMINATED_BE = 0x4C535442;
	public static final int TYPE_LIST_ZERO_TERMINATED_BE = 0x4C53545A;
	public static final int TYPE_LIST_COUNTED_BE = 0x4C535443;
	public static final int TYPE_LIST_END_BE = 0x4C535445;
	
	public static final int TYPE_HEADER_BE = 0x48454144;
	public static final int TYPE_LABEL_BE = 0x4C41424C;
	public static final int TYPE_COMMENT_BE = 0x434D4E54;
	public static final int TYPE_SEPARATOR_BE = 0x53455041;
	public static final int TYPE_META_BE = 0x4D455441;
	public static final int TYPE_IF_BE = 0x49462020;
	public static final int TYPE_ELSE_IF_BE = 0x454C4946;
	public static final int TYPE_ELSE_BE = 0x454C5345;
	public static final int TYPE_END_IF_BE = 0x454E4946;
	public static final int TYPE_TEXT_ENCODING_BE = 0x54454E43;
	
	public static final int TYPE_DEC_NIBBLE_LE = 0x42494E44;
	public static final int TYPE_DEC_BYTE_LE = 0x54594244;
	public static final int TYPE_DEC_WORD_LE = 0x44525744;
	public static final int TYPE_DEC_LONG_LE = 0x474E4C44;
	public static final int TYPE_DEC_LONG_LONG_LE = 0x474C4C44;
	public static final int TYPE_DEC_SINT4_LE = 0x34305344;
	public static final int TYPE_DEC_SINT8_LE = 0x38305344;
	public static final int TYPE_DEC_SINT16_LE = 0x36315344;
	public static final int TYPE_DEC_SINT24_LE = 0x34325344;
	public static final int TYPE_DEC_SINT32_LE = 0x32335344;
	public static final int TYPE_DEC_SINT48_LE = 0x38345344;
	public static final int TYPE_DEC_SINT64_LE = 0x34365344;
	public static final int TYPE_DEC_UINT4_LE = 0x34305544;
	public static final int TYPE_DEC_UINT8_LE = 0x38305544;
	public static final int TYPE_DEC_UINT16_LE = 0x36315544;
	public static final int TYPE_DEC_UINT24_LE = 0x34325544;
	public static final int TYPE_DEC_UINT32_LE = 0x32335544;
	public static final int TYPE_DEC_UINT48_LE = 0x38345544;
	public static final int TYPE_DEC_UINT64_LE = 0x34365544;

	public static final int TYPE_HEX_NIBBLE_LE = 0x42494E48;
	public static final int TYPE_HEX_BYTE_LE = 0x54594248;
	public static final int TYPE_HEX_WORD_LE = 0x44525748;
	public static final int TYPE_HEX_LONG_LE = 0x474E4C48;
	public static final int TYPE_HEX_LONG_LONG_LE = 0x474C4C48;
	public static final int TYPE_HEX_SINT4_LE = 0x34305348;
	public static final int TYPE_HEX_SINT8_LE = 0x38305348;
	public static final int TYPE_HEX_SINT16_LE = 0x36315348;
	public static final int TYPE_HEX_SINT24_LE = 0x34325348;
	public static final int TYPE_HEX_SINT32_LE = 0x32335348;
	public static final int TYPE_HEX_SINT48_LE = 0x38345348;
	public static final int TYPE_HEX_SINT64_LE = 0x34365348;
	public static final int TYPE_HEX_UINT4_LE = 0x34305548;
	public static final int TYPE_HEX_UINT8_LE = 0x38305548;
	public static final int TYPE_HEX_UINT16_LE = 0x36315548;
	public static final int TYPE_HEX_UINT24_LE = 0x34325548;
	public static final int TYPE_HEX_UINT32_LE = 0x32335548;
	public static final int TYPE_HEX_UINT48_LE = 0x38345548;
	public static final int TYPE_HEX_UINT64_LE = 0x34365548;

	public static final int TYPE_OCT_NIBBLE_LE = 0x42494E4F;
	public static final int TYPE_OCT_BYTE_LE = 0x5459424F;
	public static final int TYPE_OCT_WORD_LE = 0x4452574F;
	public static final int TYPE_OCT_LONG_LE = 0x474E4C4F;
	public static final int TYPE_OCT_LONG_LONG_LE = 0x474C4C4F;
	public static final int TYPE_OCT_SINT4_LE = 0x3430534F;
	public static final int TYPE_OCT_SINT8_LE = 0x3830534F;
	public static final int TYPE_OCT_SINT16_LE = 0x3631534F;
	public static final int TYPE_OCT_SINT24_LE = 0x3432534F;
	public static final int TYPE_OCT_SINT32_LE = 0x3233534F;
	public static final int TYPE_OCT_SINT48_LE = 0x3834534F;
	public static final int TYPE_OCT_SINT64_LE = 0x3436534F;
	public static final int TYPE_OCT_UINT4_LE = 0x3430554F;
	public static final int TYPE_OCT_UINT8_LE = 0x3830554F;
	public static final int TYPE_OCT_UINT16_LE = 0x3631554F;
	public static final int TYPE_OCT_UINT24_LE = 0x3432554F;
	public static final int TYPE_OCT_UINT32_LE = 0x3233554F;
	public static final int TYPE_OCT_UINT48_LE = 0x3834554F;
	public static final int TYPE_OCT_UINT64_LE = 0x3436554F;

	public static final int TYPE_BIN_NIBBLE_LE = 0x42494E42;
	public static final int TYPE_BIN_BYTE_LE = 0x54594242;
	public static final int TYPE_BIN_WORD_LE = 0x44525742;
	public static final int TYPE_BIN_LONG_LE = 0x474E4C42;
	public static final int TYPE_BIN_LONG_LONG_LE = 0x474C4C42;
	public static final int TYPE_BIN_SINT4_LE = 0x34305342;
	public static final int TYPE_BIN_SINT8_LE = 0x38305342;
	public static final int TYPE_BIN_SINT16_LE = 0x36315342;
	public static final int TYPE_BIN_SINT24_LE = 0x34325342;
	public static final int TYPE_BIN_SINT32_LE = 0x32335342;
	public static final int TYPE_BIN_SINT48_LE = 0x38345342;
	public static final int TYPE_BIN_SINT64_LE = 0x34365342;
	public static final int TYPE_BIN_UINT4_LE = 0x34305542;
	public static final int TYPE_BIN_UINT8_LE = 0x38305542;
	public static final int TYPE_BIN_UINT16_LE = 0x36315542;
	public static final int TYPE_BIN_UINT24_LE = 0x34325542;
	public static final int TYPE_BIN_UINT32_LE = 0x32335542;
	public static final int TYPE_BIN_UINT48_LE = 0x38345542;
	public static final int TYPE_BIN_UINT64_LE = 0x34365542;

	public static final int TYPE_ALIGN_NIBBLE_LE = 0x42494E41;
	public static final int TYPE_ALIGN_BYTE_LE = 0x54594241;
	public static final int TYPE_ALIGN_WORD_LE = 0x44525741;
	public static final int TYPE_ALIGN_LONG_LE = 0x474E4C41;
	public static final int TYPE_ALIGN_LONG_LONG_LE = 0x474C4C41;
	public static final int TYPE_ALIGN4_LE = 0x34304C41;
	public static final int TYPE_ALIGN8_LE = 0x38304C41;
	public static final int TYPE_ALIGN16_LE = 0x36314C41;
	public static final int TYPE_ALIGN24_LE = 0x34324C41;
	public static final int TYPE_ALIGN32_LE = 0x32334C41;
	public static final int TYPE_ALIGN48_LE = 0x38344C41;
	public static final int TYPE_ALIGN64_LE = 0x34364C41;
	
	public static final int TYPE_FILLER_BIT_LE = 0x54494246;
	public static final int TYPE_FILLER_NIBBLE_LE = 0x42494E46;
	public static final int TYPE_FILLER_BYTE_LE = 0x54594246;
	public static final int TYPE_FILLER_WORD_LE = 0x44525746;
	public static final int TYPE_FILLER_LONG_LE = 0x474E4C46;
	public static final int TYPE_FILLER_LONG_LONG_LE = 0x474C4C46;
	public static final int TYPE_FILLER1_LE = 0x31304C46;
	public static final int TYPE_FILLER4_LE = 0x34304C46;
	public static final int TYPE_FILLER8_LE = 0x38304C46;
	public static final int TYPE_FILLER16_LE = 0x36314C46;
	public static final int TYPE_FILLER24_LE = 0x34324C46;
	public static final int TYPE_FILLER32_LE = 0x32334C46;
	public static final int TYPE_FILLER48_LE = 0x38344C46;
	public static final int TYPE_FILLER64_LE = 0x34364C46;
	
	public static final int TYPE_BOOLEAN_BIT_LE = 0x54494242;
	public static final int TYPE_BOOLEAN_WORD_LE = 0x4C4F4F42;
	public static final int TYPE_BOOLEAN1_LE = 0x31304C42;
	public static final int TYPE_BOOLEAN4_LE = 0x34304C42;
	public static final int TYPE_BOOLEAN8_LE = 0x38304C42;
	public static final int TYPE_BOOLEAN16_LE = 0x36314C42;
	public static final int TYPE_BOOLEAN24_LE = 0x34324C42;
	public static final int TYPE_BOOLEAN32_LE = 0x32334C42;
	public static final int TYPE_BOOLEAN48_LE = 0x38344C42;
	public static final int TYPE_BOOLEAN64_LE = 0x34364C42;

	public static final int TYPE_FIXED4_LE = 0x34305846;
	public static final int TYPE_FIXED8_LE = 0x38305846;
	public static final int TYPE_FIXED16_LE = 0x36315846;
	public static final int TYPE_FIXED24_LE = 0x34325846;
	public static final int TYPE_FIXED32_LE = 0x32335846;
	public static final int TYPE_FIXED48_LE = 0x38345846;
	public static final int TYPE_FIXED64_LE = 0x34365846;
	
	public static final int TYPE_FLOAT_QUARTER_LE = 0x52415551;
	public static final int TYPE_FLOAT_HALF_LE = 0x464C4148;
	public static final int TYPE_FLOAT_3QUARTER_LE = 0x52545133;
	public static final int TYPE_FLOAT_SINGLE_LE = 0x474E4953;
	public static final int TYPE_FLOAT_DOUBLE_LE = 0x42554F44;
	
	public static final int TYPE_ENUM_BIT_LE = 0x54494245;
	public static final int TYPE_ENUM_NIBBLE_LE = 0x42494E45;
	public static final int TYPE_ENUM_BYTE_LE = 0x54594245;
	public static final int TYPE_ENUM_WORD_LE = 0x44525745;
	public static final int TYPE_ENUM_LONG_LE = 0x474E4C45;
	public static final int TYPE_ENUM_LONG_LONG_LE = 0x474C4C45;
	public static final int TYPE_ENUM1_LE = 0x31304E45;
	public static final int TYPE_ENUM4_LE = 0x34304E45;
	public static final int TYPE_ENUM8_LE = 0x38304E45;
	public static final int TYPE_ENUM16_LE = 0x36314E45;
	public static final int TYPE_ENUM24_LE = 0x34324E45;
	public static final int TYPE_ENUM32_LE = 0x32334E45;
	public static final int TYPE_ENUM48_LE = 0x38344E45;
	public static final int TYPE_ENUM64_LE = 0x34364E45;
	public static final int TYPE_ENUM_OPTION_LE = 0x54504F45;
	
	public static final int TYPE_SYMBOL_CHAR_LE = 0x52414843;
	public static final int TYPE_SYMBOL_TYPENAME_LE = 0x4D414E54;
	public static final int TYPE_SYMBOL_SYMBOL_LE = 0x424D5953;
	public static final int TYPE_SYMBOL8_LE = 0x38304843;
	public static final int TYPE_SYMBOL16_LE = 0x36314843;
	public static final int TYPE_SYMBOL24_LE = 0x34324843;
	public static final int TYPE_SYMBOL32_LE = 0x32334843;
	public static final int TYPE_SYMBOL48_LE = 0x38344843;
	public static final int TYPE_SYMBOL64_LE = 0x34364843;
	
	public static final int TYPE_STRING_PASCAL8_LE = 0x52545350;
	public static final int TYPE_STRING_PASCAL16_LE = 0x52545357;
	public static final int TYPE_STRING_PASCAL24_LE = 0x5254534D;
	public static final int TYPE_STRING_PASCAL32_LE = 0x5254534C;
	public static final int TYPE_STRING_PASCAL48_LE = 0x52545336;
	public static final int TYPE_STRING_PASCAL64_LE = 0x52545338;
	public static final int TYPE_STRING_PASCAL8_EVEN_LE = 0x52545345;
	public static final int TYPE_STRING_PASCAL8_ODD_LE = 0x5254534F;
	public static final int TYPE_STRING_PASCAL8_FIXED_LE = 0x00000050;
	public static final int TYPE_STRING_C_LE = 0x52545343;
	public static final int TYPE_STRING_C_EVEN_LE = 0x54534345;
	public static final int TYPE_STRING_C_ODD_LE = 0x5453434F;
	public static final int TYPE_STRING_C_FIXED_LE = 0x00000043;
	
	public static final int TYPE_HEX_DUMP_LE = 0x44584548;
	public static final int TYPE_HEX_FIXED_LE = 0x00000048;
	
	public static final int TYPE_COORDINATE_2D_LE = 0x54504432;
	public static final int TYPE_COORDINATE_3D_LE = 0x54504433;
	public static final int TYPE_RECTANGLE_LE = 0x54434552;
	
	public static final int TYPE_COLOR_RGB555_LE = 0x35353552;
	public static final int TYPE_COLOR_RGB565_LE = 0x35363552;
	public static final int TYPE_COLOR_RGB444_LE = 0x34343452;
	public static final int TYPE_COLOR_RGB888_LE = 0x38383852;
	public static final int TYPE_COLOR_RGB161616_LE = 0x58363152;
	public static final int TYPE_COLOR_BGR555_LE = 0x35523535;
	public static final int TYPE_COLOR_BGR565_LE = 0x35523635;
	public static final int TYPE_COLOR_BGR444_LE = 0x34523434;
	public static final int TYPE_COLOR_BGR888_LE = 0x38523838;
	public static final int TYPE_COLOR_BGR161616_LE = 0x36315258;
	
	public static final int TYPE_LISTCOUNTER_ZEROBASED_LE = 0x544E435A;
	public static final int TYPE_LISTCOUNTER4_ZEROBASED_LE = 0x3430435A;
	public static final int TYPE_LISTCOUNTER8_ZEROBASED_LE = 0x3830435A;
	public static final int TYPE_LISTCOUNTER16_ZEROBASED_LE = 0x3631435A;
	public static final int TYPE_LISTCOUNTER24_ZEROBASED_LE = 0x3432435A;
	public static final int TYPE_LISTCOUNTER32_ZEROBASED_LE = 0x3233435A;
	public static final int TYPE_LISTCOUNTER48_ZEROBASED_LE = 0x3834435A;
	public static final int TYPE_LISTCOUNTER64_ZEROBASED_LE = 0x3436435A;
	
	public static final int TYPE_LISTCOUNTER_ONEBASED_LE = 0x544E434F;
	public static final int TYPE_LISTCOUNTER4_ONEBASED_LE = 0x3430434F;
	public static final int TYPE_LISTCOUNTER8_ONEBASED_LE = 0x3830434F;
	public static final int TYPE_LISTCOUNTER16_ONEBASED_LE = 0x3631434F;
	public static final int TYPE_LISTCOUNTER24_ONEBASED_LE = 0x3432434F;
	public static final int TYPE_LISTCOUNTER32_ONEBASED_LE = 0x3233434F;
	public static final int TYPE_LISTCOUNTER48_ONEBASED_LE = 0x3834434F;
	public static final int TYPE_LISTCOUNTER64_ONEBASED_LE = 0x3436434F;
	
	public static final int TYPE_LIST_EOF_TERMINATED_LE = 0x4254534C;
	public static final int TYPE_LIST_ZERO_TERMINATED_LE = 0x5A54534C;
	public static final int TYPE_LIST_COUNTED_LE = 0x4354534C;
	public static final int TYPE_LIST_END_LE = 0x4554534C;
	
	public static final int TYPE_HEADER_LE = 0x44414548;
	public static final int TYPE_LABEL_LE = 0x4C42414C;
	public static final int TYPE_SEPARATOR_LE = 0x41504553;
	public static final int TYPE_COMMENT_LE = 0x544E4D43;
	public static final int TYPE_META_LE = 0x4154454D;
	public static final int TYPE_IF_LE = 0x20204649;
	public static final int TYPE_ELSE_IF_LE = 0x46494C45;
	public static final int TYPE_ELSE_LE = 0x45534C45;
	public static final int TYPE_END_IF_LE = 0x46494E45;
	public static final int TYPE_TEXT_ENCODING_LE = 0x434E4554;
	
	public static boolean isDecimalType(int t) {
		return (
				(t == TYPE_DEC_NIBBLE_BE) ||
				(t == TYPE_DEC_BYTE_BE) ||
				(t == TYPE_DEC_WORD_BE) ||
				(t == TYPE_DEC_LONG_BE) ||
				(t == TYPE_DEC_LONG_LONG_BE) ||
				(t == TYPE_DEC_NIBBLE_LE) ||
				(t == TYPE_DEC_BYTE_LE) ||
				(t == TYPE_DEC_WORD_LE) ||
				(t == TYPE_DEC_LONG_LE) ||
				(t == TYPE_DEC_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x44530000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0xFFFF0000) == 0x44550000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00005344) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16)) ||
				(((t & 0x0000FFFF) == 0x00005544) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isHexType(int t) {
		return (
				(t == TYPE_HEX_NIBBLE_BE) ||
				(t == TYPE_HEX_BYTE_BE) ||
				(t == TYPE_HEX_WORD_BE) ||
				(t == TYPE_HEX_LONG_BE) ||
				(t == TYPE_HEX_LONG_LONG_BE) ||
				(t == TYPE_HEX_NIBBLE_LE) ||
				(t == TYPE_HEX_BYTE_LE) ||
				(t == TYPE_HEX_WORD_LE) ||
				(t == TYPE_HEX_LONG_LE) ||
				(t == TYPE_HEX_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x48530000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0xFFFF0000) == 0x48550000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00005348) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16)) ||
				(((t & 0x0000FFFF) == 0x00005548) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isOctalType(int t) {
		return (
				(t == TYPE_OCT_NIBBLE_BE) ||
				(t == TYPE_OCT_BYTE_BE) ||
				(t == TYPE_OCT_WORD_BE) ||
				(t == TYPE_OCT_LONG_BE) ||
				(t == TYPE_OCT_LONG_LONG_BE) ||
				(t == TYPE_OCT_NIBBLE_LE) ||
				(t == TYPE_OCT_BYTE_LE) ||
				(t == TYPE_OCT_WORD_LE) ||
				(t == TYPE_OCT_LONG_LE) ||
				(t == TYPE_OCT_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x4F530000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0xFFFF0000) == 0x4F550000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x0000534F) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16)) ||
				(((t & 0x0000FFFF) == 0x0000554F) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isBinaryType(int t) {
		return (
				(t == TYPE_BIN_NIBBLE_BE) ||
				(t == TYPE_BIN_BYTE_BE) ||
				(t == TYPE_BIN_WORD_BE) ||
				(t == TYPE_BIN_LONG_BE) ||
				(t == TYPE_BIN_LONG_LONG_BE) ||
				(t == TYPE_BIN_NIBBLE_LE) ||
				(t == TYPE_BIN_BYTE_LE) ||
				(t == TYPE_BIN_WORD_LE) ||
				(t == TYPE_BIN_LONG_LE) ||
				(t == TYPE_BIN_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x42530000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0xFFFF0000) == 0x42550000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00005342) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16)) ||
				(((t & 0x0000FFFF) == 0x00005542) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isAlignType(int t) {
		return (
				(t == TYPE_ALIGN_NIBBLE_BE) ||
				(t == TYPE_ALIGN_BYTE_BE) ||
				(t == TYPE_ALIGN_WORD_BE) ||
				(t == TYPE_ALIGN_LONG_BE) ||
				(t == TYPE_ALIGN_LONG_LONG_BE) ||
				(t == TYPE_ALIGN_NIBBLE_LE) ||
				(t == TYPE_ALIGN_BYTE_LE) ||
				(t == TYPE_ALIGN_WORD_LE) ||
				(t == TYPE_ALIGN_LONG_LE) ||
				(t == TYPE_ALIGN_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x414C0000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00004C41) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isFillerType(int t) {
		return (
				(t == TYPE_FILLER_BIT_BE) ||
				(t == TYPE_FILLER_NIBBLE_BE) ||
				(t == TYPE_FILLER_BYTE_BE) ||
				(t == TYPE_FILLER_WORD_BE) ||
				(t == TYPE_FILLER_LONG_BE) ||
				(t == TYPE_FILLER_LONG_LONG_BE) ||
				(t == TYPE_FILLER_BIT_LE) ||
				(t == TYPE_FILLER_NIBBLE_LE) ||
				(t == TYPE_FILLER_BYTE_LE) ||
				(t == TYPE_FILLER_WORD_LE) ||
				(t == TYPE_FILLER_LONG_LE) ||
				(t == TYPE_FILLER_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x464C0000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00004C46) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isBooleanType(int t) {
		return (
				(t == TYPE_BOOLEAN_BIT_BE) ||
				(t == TYPE_BOOLEAN_WORD_BE) ||
				(t == TYPE_BOOLEAN_BIT_LE) ||
				(t == TYPE_BOOLEAN_WORD_LE) ||
				(((t & 0xFFFF0000) == 0x424C0000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00004C42) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isFixedType(int t) {
		return (
				(((t & 0xFFFF0000) == 0x46580000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00005846) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isFloatType(int t) {
		return (
				(t == TYPE_FLOAT_QUARTER_BE) ||
				(t == TYPE_FLOAT_HALF_BE) ||
				(t == TYPE_FLOAT_3QUARTER_BE) ||
				(t == TYPE_FLOAT_SINGLE_BE) ||
				(t == TYPE_FLOAT_DOUBLE_BE) ||
				(t == TYPE_FLOAT_QUARTER_LE) ||
				(t == TYPE_FLOAT_HALF_LE) ||
				(t == TYPE_FLOAT_3QUARTER_LE) ||
				(t == TYPE_FLOAT_SINGLE_LE) ||
				(t == TYPE_FLOAT_DOUBLE_LE)
		);
	}
	
	public static boolean isEnumerationType(int t) {
		return (
				(t == TYPE_ENUM_BIT_BE) ||
				(t == TYPE_ENUM_NIBBLE_BE) ||
				(t == TYPE_ENUM_BYTE_BE) ||
				(t == TYPE_ENUM_WORD_BE) ||
				(t == TYPE_ENUM_LONG_BE) ||
				(t == TYPE_ENUM_LONG_LONG_BE) ||
				(t == TYPE_ENUM_BIT_LE) ||
				(t == TYPE_ENUM_NIBBLE_LE) ||
				(t == TYPE_ENUM_BYTE_LE) ||
				(t == TYPE_ENUM_WORD_LE) ||
				(t == TYPE_ENUM_LONG_LE) ||
				(t == TYPE_ENUM_LONG_LONG_LE) ||
				(((t & 0xFFFF0000) == 0x454E0000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00004E45) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isEnumOptionType(int t) {
		return (
				(t == TYPE_ENUM_OPTION_BE) ||
				(t == TYPE_ENUM_OPTION_LE)
		);
	}
	
	public static boolean isSymbolType(int t) {
		return (
				(t == TYPE_SYMBOL_CHAR_BE) ||
				(t == TYPE_SYMBOL_TYPENAME_BE) ||
				(t == TYPE_SYMBOL_SYMBOL_BE) ||
				(t == TYPE_SYMBOL_CHAR_LE) ||
				(t == TYPE_SYMBOL_TYPENAME_LE) ||
				(t == TYPE_SYMBOL_SYMBOL_LE) ||
				(((t & 0xFFFF0000) == 0x43480000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x00004843) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	private static boolean isHexDigit(int ch) {
		return Character.isDigit(ch) || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
	}
	
	public static boolean isStringType(int t) {
		return (
				(t == TYPE_STRING_PASCAL8_BE) ||
				(t == TYPE_STRING_PASCAL16_BE) ||
				(t == TYPE_STRING_PASCAL24_BE) ||
				(t == TYPE_STRING_PASCAL32_BE) ||
				(t == TYPE_STRING_PASCAL48_BE) ||
				(t == TYPE_STRING_PASCAL64_BE) ||
				(t == TYPE_STRING_PASCAL8_EVEN_BE) ||
				(t == TYPE_STRING_PASCAL8_ODD_BE) ||
				(t == TYPE_STRING_C_BE) ||
				(t == TYPE_STRING_C_EVEN_BE) ||
				(t == TYPE_STRING_C_ODD_BE) ||
				(t == TYPE_STRING_PASCAL8_LE) ||
				(t == TYPE_STRING_PASCAL16_LE) ||
				(t == TYPE_STRING_PASCAL24_LE) ||
				(t == TYPE_STRING_PASCAL32_LE) ||
				(t == TYPE_STRING_PASCAL48_LE) ||
				(t == TYPE_STRING_PASCAL64_LE) ||
				(t == TYPE_STRING_PASCAL8_EVEN_LE) ||
				(t == TYPE_STRING_PASCAL8_ODD_LE) ||
				(t == TYPE_STRING_C_LE) ||
				(t == TYPE_STRING_C_EVEN_LE) ||
				(t == TYPE_STRING_C_ODD_LE) ||
				(((t & 0xFF000000) == 0x50000000) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8) && isHexDigit(t & 0xFF)) ||
				(((t & 0x000000FF) == 0x00000050) && isHexDigit((t & 0xFF000000) >>> 24) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8)) ||
				(((t & 0xFF000000) == 0x43000000) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8) && isHexDigit(t & 0xFF)) ||
				(((t & 0x000000FF) == 0x00000043) && isHexDigit((t & 0xFF000000) >>> 24) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8))
		);
	}
	
	public static boolean isHexDumpType(int t) {
		return (
				(t == TYPE_HEX_DUMP_BE) ||
				(t == TYPE_HEX_DUMP_LE)
		);
	}
	
	public static boolean isHexStringType(int t) {
		return (
				(((t & 0xFF000000) == 0x48000000) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8) && isHexDigit(t & 0xFF)) ||
				(((t & 0x000000FF) == 0x00000048) && isHexDigit((t & 0xFF000000) >>> 24) && isHexDigit((t & 0xFF0000) >>> 16) && isHexDigit((t & 0xFF00) >>> 8))
		);
	}
	
	public static boolean isCoordinateType(int t) {
		return (
				(t == TYPE_COORDINATE_2D_BE) ||
				(t == TYPE_COORDINATE_3D_BE) ||
				(t == TYPE_RECTANGLE_BE) ||
				(t == TYPE_COORDINATE_2D_LE) ||
				(t == TYPE_COORDINATE_3D_LE) ||
				(t == TYPE_RECTANGLE_LE)
		);
	}
	
	public static boolean isColorType(int t) {
		return (
				(t == TYPE_COLOR_RGB555_BE) ||
				(t == TYPE_COLOR_RGB565_BE) ||
				(t == TYPE_COLOR_RGB444_BE) ||
				(t == TYPE_COLOR_RGB888_BE) ||
				(t == TYPE_COLOR_RGB161616_BE) ||
				(t == TYPE_COLOR_BGR555_BE) ||
				(t == TYPE_COLOR_BGR565_BE) ||
				(t == TYPE_COLOR_BGR444_BE) ||
				(t == TYPE_COLOR_BGR888_BE) ||
				(t == TYPE_COLOR_BGR161616_BE) ||
				(t == TYPE_COLOR_RGB555_LE) ||
				(t == TYPE_COLOR_RGB565_LE) ||
				(t == TYPE_COLOR_RGB444_LE) ||
				(t == TYPE_COLOR_RGB888_LE) ||
				(t == TYPE_COLOR_RGB161616_LE) ||
				(t == TYPE_COLOR_BGR555_LE) ||
				(t == TYPE_COLOR_BGR565_LE) ||
				(t == TYPE_COLOR_BGR444_LE) ||
				(t == TYPE_COLOR_BGR888_LE) ||
				(t == TYPE_COLOR_BGR161616_LE)
		);
	}
	
	public static boolean isListCounterType(int t) {
		return (
				(t == TYPE_LISTCOUNTER_ZEROBASED_BE) ||
				(t == TYPE_LISTCOUNTER_ONEBASED_BE) ||
				(t == TYPE_LISTCOUNTER_ZEROBASED_LE) ||
				(t == TYPE_LISTCOUNTER_ONEBASED_LE) ||
				(((t & 0xFFFF0000) == 0x4F430000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x0000434F) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16)) ||
				(((t & 0xFFFF0000) == 0x5A430000) && Character.isDigit((t & 0xFF00) >>> 8) && Character.isDigit(t & 0xFF)) ||
				(((t & 0x0000FFFF) == 0x0000435A) && Character.isDigit((t & 0xFF000000) >>> 24) && Character.isDigit((t & 0xFF0000) >>> 16))
		);
	}
	
	public static boolean isEOFTerminatedListType(int t) {
		return (
				(t == TYPE_LIST_EOF_TERMINATED_BE) ||
				(t == TYPE_LIST_EOF_TERMINATED_LE)
		);
	}
	
	public static boolean isZeroTerminatedListType(int t) {
		return (
				(t == TYPE_LIST_ZERO_TERMINATED_BE) ||
				(t == TYPE_LIST_ZERO_TERMINATED_LE)
		);
	}
	
	public static boolean isCountedListType(int t) {
		return (
				(t == TYPE_LIST_COUNTED_BE) ||
				(t == TYPE_LIST_COUNTED_LE)
		);
	}
	
	public static boolean isListBeginType(int t) {
		return (
				(t == TYPE_LIST_EOF_TERMINATED_BE) ||
				(t == TYPE_LIST_EOF_TERMINATED_LE) ||
				(t == TYPE_LIST_ZERO_TERMINATED_BE) ||
				(t == TYPE_LIST_ZERO_TERMINATED_LE) ||
				(t == TYPE_LIST_COUNTED_BE) ||
				(t == TYPE_LIST_COUNTED_LE)
		);
	}
	
	public static boolean isListEndType(int t) {
		return (
				(t == TYPE_LIST_END_BE) ||
				(t == TYPE_LIST_END_LE)
		);
	}
	
	public static boolean isDisplayType(int t) {
		return (
				(t == TYPE_HEADER_BE) ||
				(t == TYPE_HEADER_LE) ||
				(t == TYPE_LABEL_BE) ||
				(t == TYPE_LABEL_LE) ||
				(t == TYPE_SEPARATOR_BE) ||
				(t == TYPE_SEPARATOR_LE)
		);
	}
	
	public static boolean isHeaderType(int t) {
		return (
				(t == TYPE_HEADER_BE) ||
				(t == TYPE_HEADER_LE)
		);
	}
	
	public static boolean isLabelType(int t) {
		return (
				(t == TYPE_LABEL_BE) ||
				(t == TYPE_LABEL_LE)
		);
	}
	
	public static boolean isSeparatorType(int t) {
		return (
				(t == TYPE_SEPARATOR_BE) ||
				(t == TYPE_SEPARATOR_LE)
		);
	}
	
	public static boolean isCommentType(int t) {
		return (
				(t == TYPE_COMMENT_BE) ||
				(t == TYPE_COMMENT_LE)
		);
	}
	
	public static boolean isMetaType(int t) {
		return (
				(t == TYPE_META_BE) ||
				(t == TYPE_META_LE)
		);
	}
	
	public static boolean isIfType(int t) {
		return (
				(t == TYPE_IF_BE) ||
				(t == TYPE_IF_LE)
		);
	}
	
	public static boolean isElseIfType(int t) {
		return (
				(t == TYPE_ELSE_IF_BE) ||
				(t == TYPE_ELSE_IF_LE)
		);
	}
	
	public static boolean isElseType(int t) {
		return (
				(t == TYPE_ELSE_BE) ||
				(t == TYPE_ELSE_LE)
		);
	}
	
	public static boolean isEndIfType(int t) {
		return (
				(t == TYPE_END_IF_BE) ||
				(t == TYPE_END_IF_LE)
		);
	}
	
	public static boolean isTextEncodingType(int t) {
		return (
				(t == TYPE_TEXT_ENCODING_BE) ||
				(t == TYPE_TEXT_ENCODING_LE)
		);
	}
	
	protected int type;
	protected String name;
	
	public TemplateField(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public TFEditor createEditor(String tenc) {
		switch (type) {
		case TYPE_DEC_NIBBLE_BE: return new TFEditorInteger(4, 10, false, false);
		case TYPE_DEC_BYTE_BE: return new TFEditorInteger(8, 10, false, false);
		case TYPE_DEC_WORD_BE: return new TFEditorInteger(16, 10, false, false);
		case TYPE_DEC_LONG_BE: return new TFEditorInteger(32, 10, false, false);
		case TYPE_DEC_LONG_LONG_BE: return new TFEditorInteger(64, 10, false, false);
		case TYPE_DEC_NIBBLE_LE: return new TFEditorInteger(4, 10, false, true);
		case TYPE_DEC_BYTE_LE: return new TFEditorInteger(8, 10, false, true);
		case TYPE_DEC_WORD_LE: return new TFEditorInteger(16, 10, false, true);
		case TYPE_DEC_LONG_LE: return new TFEditorInteger(32, 10, false, true);
		case TYPE_DEC_LONG_LONG_LE: return new TFEditorInteger(64, 10, false, true);
		case TYPE_HEX_NIBBLE_BE: return new TFEditorInteger(4, 16, true, false);
		case TYPE_HEX_BYTE_BE: return new TFEditorInteger(8, 16, true, false);
		case TYPE_HEX_WORD_BE: return new TFEditorInteger(16, 16, true, false);
		case TYPE_HEX_LONG_BE: return new TFEditorInteger(32, 16, true, false);
		case TYPE_HEX_LONG_LONG_BE: return new TFEditorInteger(64, 16, true, false);
		case TYPE_HEX_NIBBLE_LE: return new TFEditorInteger(4, 16, true, true);
		case TYPE_HEX_BYTE_LE: return new TFEditorInteger(8, 16, true, true);
		case TYPE_HEX_WORD_LE: return new TFEditorInteger(16, 16, true, true);
		case TYPE_HEX_LONG_LE: return new TFEditorInteger(32, 16, true, true);
		case TYPE_HEX_LONG_LONG_LE: return new TFEditorInteger(64, 16, true, true);
		case TYPE_OCT_NIBBLE_BE: return new TFEditorInteger(4, 8, true, false);
		case TYPE_OCT_BYTE_BE: return new TFEditorInteger(8, 8, true, false);
		case TYPE_OCT_WORD_BE: return new TFEditorInteger(16, 8, true, false);
		case TYPE_OCT_LONG_BE: return new TFEditorInteger(32, 8, true, false);
		case TYPE_OCT_LONG_LONG_BE: return new TFEditorInteger(64, 8, true, false);
		case TYPE_OCT_NIBBLE_LE: return new TFEditorInteger(4, 8, true, true);
		case TYPE_OCT_BYTE_LE: return new TFEditorInteger(8, 8, true, true);
		case TYPE_OCT_WORD_LE: return new TFEditorInteger(16, 8, true, true);
		case TYPE_OCT_LONG_LE: return new TFEditorInteger(32, 8, true, true);
		case TYPE_OCT_LONG_LONG_LE: return new TFEditorInteger(64, 8, true, true);
		case TYPE_BIN_NIBBLE_BE: return new TFEditorInteger(4, 2, true, false);
		case TYPE_BIN_BYTE_BE: return new TFEditorInteger(8, 2, true, false);
		case TYPE_BIN_WORD_BE: return new TFEditorInteger(16, 2, true, false);
		case TYPE_BIN_LONG_BE: return new TFEditorInteger(32, 2, true, false);
		case TYPE_BIN_LONG_LONG_BE: return new TFEditorInteger(64, 2, true, false);
		case TYPE_BIN_NIBBLE_LE: return new TFEditorInteger(4, 2, true, true);
		case TYPE_BIN_BYTE_LE: return new TFEditorInteger(8, 2, true, true);
		case TYPE_BIN_WORD_LE: return new TFEditorInteger(16, 2, true, true);
		case TYPE_BIN_LONG_LE: return new TFEditorInteger(32, 2, true, true);
		case TYPE_BIN_LONG_LONG_LE: return new TFEditorInteger(64, 2, true, true);
		case TYPE_ALIGN_NIBBLE_BE: return new TFEditorAlignment(4);
		case TYPE_ALIGN_BYTE_BE: return new TFEditorAlignment(8);
		case TYPE_ALIGN_WORD_BE: return new TFEditorAlignment(16);
		case TYPE_ALIGN_LONG_BE: return new TFEditorAlignment(32);
		case TYPE_ALIGN_LONG_LONG_BE: return new TFEditorAlignment(64);
		case TYPE_ALIGN_NIBBLE_LE: return new TFEditorAlignment(4);
		case TYPE_ALIGN_BYTE_LE: return new TFEditorAlignment(8);
		case TYPE_ALIGN_WORD_LE: return new TFEditorAlignment(16);
		case TYPE_ALIGN_LONG_LE: return new TFEditorAlignment(32);
		case TYPE_ALIGN_LONG_LONG_LE: return new TFEditorAlignment(64);
		case TYPE_FILLER_BIT_BE: return new TFEditorFiller(1);
		case TYPE_FILLER_NIBBLE_BE: return new TFEditorFiller(4);
		case TYPE_FILLER_BYTE_BE: return new TFEditorFiller(8);
		case TYPE_FILLER_WORD_BE: return new TFEditorFiller(16);
		case TYPE_FILLER_LONG_BE: return new TFEditorFiller(32);
		case TYPE_FILLER_LONG_LONG_BE: return new TFEditorFiller(64);
		case TYPE_FILLER_BIT_LE: return new TFEditorFiller(1);
		case TYPE_FILLER_NIBBLE_LE: return new TFEditorFiller(4);
		case TYPE_FILLER_BYTE_LE: return new TFEditorFiller(8);
		case TYPE_FILLER_WORD_LE: return new TFEditorFiller(16);
		case TYPE_FILLER_LONG_LE: return new TFEditorFiller(32);
		case TYPE_FILLER_LONG_LONG_LE: return new TFEditorFiller(64);
		case TYPE_BOOLEAN_BIT_BE: return new TFEditorBoolean(1, false);
		case TYPE_BOOLEAN_WORD_BE: return new TFEditorBoolean(16, false);
		case TYPE_BOOLEAN_BIT_LE: return new TFEditorBoolean(1, true);
		case TYPE_BOOLEAN_WORD_LE: return new TFEditorBoolean(16, true);
		case TYPE_FLOAT_QUARTER_BE: return new TFEditorFloat(8, false);
		case TYPE_FLOAT_HALF_BE: return new TFEditorFloat(16, false);
		case TYPE_FLOAT_3QUARTER_BE: return new TFEditorFloat(24, false);
		case TYPE_FLOAT_SINGLE_BE: return new TFEditorFloat(32, false);
		case TYPE_FLOAT_DOUBLE_BE: return new TFEditorFloat(64, false);
		case TYPE_FLOAT_QUARTER_LE: return new TFEditorFloat(8, true);
		case TYPE_FLOAT_HALF_LE: return new TFEditorFloat(16, true);
		case TYPE_FLOAT_3QUARTER_LE: return new TFEditorFloat(24, true);
		case TYPE_FLOAT_SINGLE_LE: return new TFEditorFloat(32, true);
		case TYPE_FLOAT_DOUBLE_LE: return new TFEditorFloat(64, true);
		case TYPE_ENUM_BIT_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_NIBBLE_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_BYTE_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_WORD_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_LONG_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_LONG_LONG_BE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_BIT_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_NIBBLE_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_BYTE_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_WORD_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_LONG_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_LONG_LONG_LE: throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
		case TYPE_ENUM_OPTION_BE: throw new IllegalArgumentException("This template is invalid. It contains an EOPT field that was not processed correctly.");
		case TYPE_ENUM_OPTION_LE: throw new IllegalArgumentException("This template is invalid. It contains an EOPT field that was not processed correctly.");
		case TYPE_SYMBOL_CHAR_BE: return new TFEditorSymbol(8, false, tenc, false, true, false);
		case TYPE_SYMBOL_TYPENAME_BE: return new TFEditorSymbol(32, false, tenc, false, true, false);
		case TYPE_SYMBOL_SYMBOL_BE: return new TFEditorSymbol(64, false, tenc, false, true, false);
		case TYPE_SYMBOL_CHAR_LE: return new TFEditorSymbol(8, true, tenc, false, true, false);
		case TYPE_SYMBOL_TYPENAME_LE: return new TFEditorSymbol(32, true, tenc, false, true, false);
		case TYPE_SYMBOL_SYMBOL_LE: return new TFEditorSymbol(64, true, tenc, false, true, false);
		case TYPE_STRING_PASCAL8_BE: return new TFEditorString(8, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL16_BE: return new TFEditorString(16, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL24_BE: return new TFEditorString(24, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL32_BE: return new TFEditorString(32, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL48_BE: return new TFEditorString(48, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL64_BE: return new TFEditorString(64, false, 0, false, false, tenc);
		case TYPE_STRING_PASCAL8_EVEN_BE: return new TFEditorString(8, false, 0, true, false, tenc);
		case TYPE_STRING_PASCAL8_ODD_BE: return new TFEditorString(8, false, 0, false, true, tenc);
		case TYPE_STRING_C_BE: return new TFEditorString(0, false, 0, false, false, tenc);
		case TYPE_STRING_C_EVEN_BE: return new TFEditorString(0, false, 0, true, false, tenc);
		case TYPE_STRING_C_ODD_BE: return new TFEditorString(0, false, 0, false, true, tenc);
		case TYPE_STRING_PASCAL8_LE: return new TFEditorString(8, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL16_LE: return new TFEditorString(16, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL24_LE: return new TFEditorString(24, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL32_LE: return new TFEditorString(32, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL48_LE: return new TFEditorString(48, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL64_LE: return new TFEditorString(64, true, 0, false, false, tenc);
		case TYPE_STRING_PASCAL8_EVEN_LE: return new TFEditorString(8, true, 0, true, false, tenc);
		case TYPE_STRING_PASCAL8_ODD_LE: return new TFEditorString(8, true, 0, false, true, tenc);
		case TYPE_STRING_C_LE: return new TFEditorString(0, true, 0, false, false, tenc);
		case TYPE_STRING_C_EVEN_LE: return new TFEditorString(0, true, 0, true, false, tenc);
		case TYPE_STRING_C_ODD_LE: return new TFEditorString(0, true, 0, false, true, tenc);
		case TYPE_HEX_DUMP_BE: return new TFEditorHex(0, false);
		case TYPE_HEX_DUMP_LE: return new TFEditorHex(0, true);
		case TYPE_COORDINATE_2D_BE: return new TFEditorCoordinates(16, false, 2, true);
		case TYPE_COORDINATE_3D_BE: return new TFEditorCoordinates(16, false, 3, true);
		case TYPE_RECTANGLE_BE: return new TFEditorCoordinates(16, false, 4, true);
		case TYPE_COORDINATE_2D_LE: return new TFEditorCoordinates(16, true, 2, false);
		case TYPE_COORDINATE_3D_LE: return new TFEditorCoordinates(16, true, 3, false);
		case TYPE_RECTANGLE_LE: return new TFEditorCoordinates(16, true, 4, false);
		case TYPE_COLOR_RGB555_BE: return new TFEditorColor(0, 5, 5, 5, TFEditorColor.FORMAT_ARGB, false, false);
		case TYPE_COLOR_RGB565_BE: return new TFEditorColor(0, 5, 6, 5, TFEditorColor.FORMAT_ARGB, false, false);
		case TYPE_COLOR_RGB444_BE: return new TFEditorColor(0, 4, 4, 4, TFEditorColor.FORMAT_ARGB, false, false);
		case TYPE_COLOR_RGB888_BE: return new TFEditorColor(0, 8, 8, 8, TFEditorColor.FORMAT_ARGB, false, false);
		case TYPE_COLOR_RGB161616_BE: return new TFEditorColor(0, 16, 16, 16, TFEditorColor.FORMAT_ARGB, false, false);
		case TYPE_COLOR_BGR555_BE: return new TFEditorColor(0, 5, 5, 5, TFEditorColor.FORMAT_ABGR, false, false);
		case TYPE_COLOR_BGR565_BE: return new TFEditorColor(0, 5, 6, 5, TFEditorColor.FORMAT_ABGR, false, false);
		case TYPE_COLOR_BGR444_BE: return new TFEditorColor(0, 4, 4, 4, TFEditorColor.FORMAT_ABGR, false, false);
		case TYPE_COLOR_BGR888_BE: return new TFEditorColor(0, 8, 8, 8, TFEditorColor.FORMAT_ABGR, false, false);
		case TYPE_COLOR_BGR161616_BE: return new TFEditorColor(0, 16, 16, 16, TFEditorColor.FORMAT_ABGR, false, false);
		case TYPE_COLOR_RGB555_LE: return new TFEditorColor(0, 5, 5, 5, TFEditorColor.FORMAT_ARGB, false, true);
		case TYPE_COLOR_RGB565_LE: return new TFEditorColor(0, 5, 6, 5, TFEditorColor.FORMAT_ARGB, false, true);
		case TYPE_COLOR_RGB444_LE: return new TFEditorColor(0, 4, 4, 4, TFEditorColor.FORMAT_ARGB, false, true);
		case TYPE_COLOR_RGB888_LE: return new TFEditorColor(0, 8, 8, 8, TFEditorColor.FORMAT_ARGB, false, true);
		case TYPE_COLOR_RGB161616_LE: return new TFEditorColor(0, 16, 16, 16, TFEditorColor.FORMAT_ARGB, false, true);
		case TYPE_COLOR_BGR555_LE: return new TFEditorColor(0, 5, 5, 5, TFEditorColor.FORMAT_ABGR, false, true);
		case TYPE_COLOR_BGR565_LE: return new TFEditorColor(0, 5, 6, 5, TFEditorColor.FORMAT_ABGR, false, true);
		case TYPE_COLOR_BGR444_LE: return new TFEditorColor(0, 4, 4, 4, TFEditorColor.FORMAT_ABGR, false, true);
		case TYPE_COLOR_BGR888_LE: return new TFEditorColor(0, 8, 8, 8, TFEditorColor.FORMAT_ABGR, false, true);
		case TYPE_COLOR_BGR161616_LE: return new TFEditorColor(0, 16, 16, 16, TFEditorColor.FORMAT_ABGR, false, true);
		case TYPE_LISTCOUNTER_ZEROBASED_BE: return new TFEditorListCount(16, false, true);
		case TYPE_LISTCOUNTER_ONEBASED_BE: return new TFEditorListCount(16, false, false);
		case TYPE_LISTCOUNTER_ZEROBASED_LE: return new TFEditorListCount(16, true, true);
		case TYPE_LISTCOUNTER_ONEBASED_LE: return new TFEditorListCount(16, true, false);
		case TYPE_LIST_EOF_TERMINATED_BE: throw new IllegalArgumentException("This template is invalid. It contains a LSTB field that was not processed correctly.");
		case TYPE_LIST_EOF_TERMINATED_LE: throw new IllegalArgumentException("This template is invalid. It contains a LSTB field that was not processed correctly.");
		case TYPE_LIST_ZERO_TERMINATED_BE: throw new IllegalArgumentException("This template is invalid. It contains a LSTZ field that was not processed correctly.");
		case TYPE_LIST_ZERO_TERMINATED_LE: throw new IllegalArgumentException("This template is invalid. It contains a LSTZ field that was not processed correctly.");
		case TYPE_LIST_COUNTED_BE: throw new IllegalArgumentException("This template is invalid. It contains a LSTC field that was not processed correctly.");
		case TYPE_LIST_COUNTED_LE: throw new IllegalArgumentException("This template is invalid. It contains a LSTC field that was not processed correctly.");
		case TYPE_LIST_END_BE: throw new IllegalArgumentException("This template is invalid. It contains a LSTE field that was not processed correctly.");
		case TYPE_LIST_END_LE: throw new IllegalArgumentException("This template is invalid. It contains a LSTE field that was not processed correctly.");
		case TYPE_HEADER_BE: throw new IllegalArgumentException("This template is invalid. It contains a HEAD field that was not processed correctly.");
		case TYPE_HEADER_LE: throw new IllegalArgumentException("This template is invalid. It contains a HEAD field that was not processed correctly.");
		case TYPE_LABEL_BE: throw new IllegalArgumentException("This template is invalid. It contains a LABL field that was not processed correctly.");
		case TYPE_LABEL_LE: throw new IllegalArgumentException("This template is invalid. It contains a LABL field that was not processed correctly.");
		case TYPE_SEPARATOR_BE: throw new IllegalArgumentException("This template is invalid. It contains a SEPA field that was not processed correctly.");
		case TYPE_SEPARATOR_LE: throw new IllegalArgumentException("This template is invalid. It contains a SEPA field that was not processed correctly.");
		case TYPE_COMMENT_BE: throw new IllegalArgumentException("This template is invalid. It contains a CMNT field that was not processed correctly.");
		case TYPE_COMMENT_LE: throw new IllegalArgumentException("This template is invalid. It contains a CMNT field that was not processed correctly.");
		case TYPE_META_BE: throw new IllegalArgumentException("This template is invalid. It contains a META field that was not processed correctly.");
		case TYPE_META_LE: throw new IllegalArgumentException("This template is invalid. It contains a META field that was not processed correctly.");
		case TYPE_IF_BE: throw new IllegalArgumentException("This template is invalid. It contains an IF field that was not processed correctly.");
		case TYPE_IF_LE: throw new IllegalArgumentException("This template is invalid. It contains an IF field that was not processed correctly.");
		case TYPE_ELSE_IF_BE: throw new IllegalArgumentException("This template is invalid. It contains an ELIF field that was not processed correctly.");
		case TYPE_ELSE_IF_LE: throw new IllegalArgumentException("This template is invalid. It contains an ELIF field that was not processed correctly.");
		case TYPE_ELSE_BE: throw new IllegalArgumentException("This template is invalid. It contains an ELSE field that was not processed correctly.");
		case TYPE_ELSE_LE: throw new IllegalArgumentException("This template is invalid. It contains an ELSE field that was not processed correctly.");
		case TYPE_END_IF_BE: throw new IllegalArgumentException("This template is invalid. It contains an ENIF field that was not processed correctly.");
		case TYPE_END_IF_LE: throw new IllegalArgumentException("This template is invalid. It contains an ENIF field that was not processed correctly.");
		case TYPE_TEXT_ENCODING_BE: throw new IllegalArgumentException("This template is invalid. It contains a TENC field that was not processed correctly.");
		case TYPE_TEXT_ENCODING_LE: throw new IllegalArgumentException("This template is invalid. It contains a TENC field that was not processed correctly.");
		default:
			if (((type & 0xFFFF0000) == 0x44530000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 10, false, false);
			else if (((type & 0xFFFF0000) == 0x44550000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 10, true, false);
			else if (((type & 0x0000FFFF) == 0x00005344) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 10, false, true);
			else if (((type & 0x0000FFFF) == 0x00005544) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 10, true, true);
			else if (((type & 0xFFFF0000) == 0x48530000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 16, false, false);
			else if (((type & 0xFFFF0000) == 0x48550000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 16, true, false);
			else if (((type & 0x0000FFFF) == 0x00005348) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 16, false, true);
			else if (((type & 0x0000FFFF) == 0x00005548) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 16, true, true);
			else if (((type & 0xFFFF0000) == 0x4F530000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 8, false, false);
			else if (((type & 0xFFFF0000) == 0x4F550000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 8, true, false);
			else if (((type & 0x0000FFFF) == 0x0000534F) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 8, false, true);
			else if (((type & 0x0000FFFF) == 0x0000554F) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 8, true, true);
			else if (((type & 0xFFFF0000) == 0x42530000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 2, false, false);
			else if (((type & 0xFFFF0000) == 0x42550000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorInteger(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), 2, true, false);
			else if (((type & 0x0000FFFF) == 0x00005342) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 2, false, true);
			else if (((type & 0x0000FFFF) == 0x00005542) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorInteger(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), 2, true, true);
			else if (((type & 0xFFFF0000) == 0x414C0000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorAlignment(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10));
			else if (((type & 0x0000FFFF) == 0x00004C41) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorAlignment(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10));
			else if (((type & 0xFFFF0000) == 0x464C0000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorFiller(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10));
			else if (((type & 0x0000FFFF) == 0x00004C46) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorFiller(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10));
			else if (((type & 0xFFFF0000) == 0x424C0000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorBoolean(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false);
			else if (((type & 0x0000FFFF) == 0x00004C42) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorBoolean(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true);
			else if (((type & 0xFFFF0000) == 0x46580000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorFixed(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false);
			else if (((type & 0x0000FFFF) == 0x00005846) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorFixed(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true);
			else if (((type & 0xFFFF0000) == 0x454E0000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
			else if (((type & 0x0000FFFF) == 0x00004E45) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				throw new IllegalArgumentException("This template is invalid. It contains an enumeration field that was not processed correctly.");
			else if (((type & 0xFFFF0000) == 0x43480000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorSymbol(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false, tenc, false, true, false);
			else if (((type & 0x0000FFFF) == 0x00004843) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorSymbol(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true, tenc, false, true, false);
			else if (((type & 0xFFFF0000) == 0x4F430000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorListCount(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false, false);
			else if (((type & 0x0000FFFF) == 0x0000434F) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorListCount(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true, false);
			else if (((type & 0xFFFF0000) == 0x5A430000) && Character.isDigit((type & 0xFF00) >>> 8) && Character.isDigit(type & 0xFF))
				return new TFEditorListCount(Character.digit((type & 0xFF00) >>> 8, 10)*10 + Character.digit(type & 0xFF, 10), false, true);
			else if (((type & 0x0000FFFF) == 0x0000435A) && Character.isDigit((type & 0xFF000000) >>> 24) && Character.isDigit((type & 0xFF0000) >>> 16))
				return new TFEditorListCount(Character.digit((type & 0xFF0000) >>> 16, 10)*10 + Character.digit((type & 0xFF000000) >>> 24, 10), true, true);
			else if (((type & 0xFF000000) == 0x50000000) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8) && isHexDigit(type & 0xFF))
				return new TFEditorString(8, false, Character.digit((type & 0xFF0000) >>> 16, 16)*64 + Character.digit((type & 0xFF00) >>> 8, 16)*16 + Character.digit(type & 0xFF, 16), false, false, tenc);
			else if (((type & 0x000000FF) == 0x00000050) && isHexDigit((type & 0xFF000000) >>> 24) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8))
				return new TFEditorString(8, true, Character.digit((type & 0xFF00) >>> 8, 16)*64 + Character.digit((type & 0xFF0000) >>> 16, 16)*16 + Character.digit((type & 0xFF000000) >>> 24, 16), false, false, tenc);
			else if (((type & 0xFF000000) == 0x43000000) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8) && isHexDigit(type & 0xFF))
				return new TFEditorString(0, false, Character.digit((type & 0xFF0000) >>> 16, 16)*64 + Character.digit((type & 0xFF00) >>> 8, 16)*16 + Character.digit(type & 0xFF, 16), false, false, tenc);
			else if (((type & 0x000000FF) == 0x00000043) && isHexDigit((type & 0xFF000000) >>> 24) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8))
				return new TFEditorString(0, true, Character.digit((type & 0xFF00) >>> 8, 16)*64 + Character.digit((type & 0xFF0000) >>> 16, 16)*16 + Character.digit((type & 0xFF000000) >>> 24, 16), false, false, tenc);
			else if (((type & 0xFF000000) == 0x48000000) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8) && isHexDigit(type & 0xFF))
				return new TFEditorHex(Character.digit((type & 0xFF0000) >>> 16, 16)*64 + Character.digit((type & 0xFF00) >>> 8, 16)*16 + Character.digit(type & 0xFF, 16), false);
			else if (((type & 0x000000FF) == 0x00000048) && isHexDigit((type & 0xFF000000) >>> 24) && isHexDigit((type & 0xFF0000) >>> 16) && isHexDigit((type & 0xFF00) >>> 8))
				return new TFEditorHex(Character.digit((type & 0xFF00) >>> 8, 16)*64 + Character.digit((type & 0xFF0000) >>> 16, 16)*16 + Character.digit((type & 0xFF000000) >>> 24, 16), true);
			else
				throw new IllegalArgumentException("This template is invalid. It contains an unknown type: "+KSFLUtilities.fccs(type)+".");
		}
	}
	
	// TODO comment out the following code before release
	
	@SuppressWarnings("unused")
	private static void uniquenessTest() {
		// This utilizes the Java compiler to ensure
		// the uniqueness of every template type and
		// the unambiguity of little- and big-endian
		// variants.
		switch (0) {
		case TYPE_DEC_NIBBLE_BE: break;
		case TYPE_DEC_BYTE_BE: break;
		case TYPE_DEC_WORD_BE: break;
		case TYPE_DEC_LONG_BE: break;
		case TYPE_DEC_LONG_LONG_BE: break;
		case TYPE_DEC_SINT4_BE: break;
		case TYPE_DEC_SINT8_BE: break;
		case TYPE_DEC_SINT16_BE: break;
		case TYPE_DEC_SINT24_BE: break;
		case TYPE_DEC_SINT32_BE: break;
		case TYPE_DEC_SINT48_BE: break;
		case TYPE_DEC_SINT64_BE: break;
		case TYPE_DEC_UINT4_BE: break;
		case TYPE_DEC_UINT8_BE: break;
		case TYPE_DEC_UINT16_BE: break;
		case TYPE_DEC_UINT24_BE: break;
		case TYPE_DEC_UINT32_BE: break;
		case TYPE_DEC_UINT48_BE: break;
		case TYPE_DEC_UINT64_BE: break;
		case TYPE_HEX_NIBBLE_BE: break;
		case TYPE_HEX_BYTE_BE: break;
		case TYPE_HEX_WORD_BE: break;
		case TYPE_HEX_LONG_BE: break;
		case TYPE_HEX_LONG_LONG_BE: break;
		case TYPE_HEX_SINT4_BE: break;
		case TYPE_HEX_SINT8_BE: break;
		case TYPE_HEX_SINT16_BE: break;
		case TYPE_HEX_SINT24_BE: break;
		case TYPE_HEX_SINT32_BE: break;
		case TYPE_HEX_SINT48_BE: break;
		case TYPE_HEX_SINT64_BE: break;
		case TYPE_HEX_UINT4_BE: break;
		case TYPE_HEX_UINT8_BE: break;
		case TYPE_HEX_UINT16_BE: break;
		case TYPE_HEX_UINT24_BE: break;
		case TYPE_HEX_UINT32_BE: break;
		case TYPE_HEX_UINT48_BE: break;
		case TYPE_HEX_UINT64_BE: break;
		case TYPE_OCT_NIBBLE_BE: break;
		case TYPE_OCT_BYTE_BE: break;
		case TYPE_OCT_WORD_BE: break;
		case TYPE_OCT_LONG_BE: break;
		case TYPE_OCT_LONG_LONG_BE: break;
		case TYPE_OCT_SINT4_BE: break;
		case TYPE_OCT_SINT8_BE: break;
		case TYPE_OCT_SINT16_BE: break;
		case TYPE_OCT_SINT24_BE: break;
		case TYPE_OCT_SINT32_BE: break;
		case TYPE_OCT_SINT48_BE: break;
		case TYPE_OCT_SINT64_BE: break;
		case TYPE_OCT_UINT4_BE: break;
		case TYPE_OCT_UINT8_BE: break;
		case TYPE_OCT_UINT16_BE: break;
		case TYPE_OCT_UINT24_BE: break;
		case TYPE_OCT_UINT32_BE: break;
		case TYPE_OCT_UINT48_BE: break;
		case TYPE_OCT_UINT64_BE: break;
		case TYPE_BIN_NIBBLE_BE: break;
		case TYPE_BIN_BYTE_BE: break;
		case TYPE_BIN_WORD_BE: break;
		case TYPE_BIN_LONG_BE: break;
		case TYPE_BIN_LONG_LONG_BE: break;
		case TYPE_BIN_SINT4_BE: break;
		case TYPE_BIN_SINT8_BE: break;
		case TYPE_BIN_SINT16_BE: break;
		case TYPE_BIN_SINT24_BE: break;
		case TYPE_BIN_SINT32_BE: break;
		case TYPE_BIN_SINT48_BE: break;
		case TYPE_BIN_SINT64_BE: break;
		case TYPE_BIN_UINT4_BE: break;
		case TYPE_BIN_UINT8_BE: break;
		case TYPE_BIN_UINT16_BE: break;
		case TYPE_BIN_UINT24_BE: break;
		case TYPE_BIN_UINT32_BE: break;
		case TYPE_BIN_UINT48_BE: break;
		case TYPE_BIN_UINT64_BE: break;
		case TYPE_ALIGN_NIBBLE_BE: break;
		case TYPE_ALIGN_BYTE_BE: break;
		case TYPE_ALIGN_WORD_BE: break;
		case TYPE_ALIGN_LONG_BE: break;
		case TYPE_ALIGN_LONG_LONG_BE: break;
		case TYPE_ALIGN4_BE: break;
		case TYPE_ALIGN8_BE: break;
		case TYPE_ALIGN16_BE: break;
		case TYPE_ALIGN24_BE: break;
		case TYPE_ALIGN32_BE: break;
		case TYPE_ALIGN48_BE: break;
		case TYPE_ALIGN64_BE: break;
		case TYPE_FILLER_BIT_BE: break;
		case TYPE_FILLER_NIBBLE_BE: break;
		case TYPE_FILLER_BYTE_BE: break;
		case TYPE_FILLER_WORD_BE: break;
		case TYPE_FILLER_LONG_BE: break;
		case TYPE_FILLER_LONG_LONG_BE: break;
		case TYPE_FILLER1_BE: break;
		case TYPE_FILLER4_BE: break;
		case TYPE_FILLER8_BE: break;
		case TYPE_FILLER16_BE: break;
		case TYPE_FILLER24_BE: break;
		case TYPE_FILLER32_BE: break;
		case TYPE_FILLER48_BE: break;
		case TYPE_FILLER64_BE: break;
		case TYPE_BOOLEAN_BIT_BE: break;
		case TYPE_BOOLEAN_WORD_BE: break;
		case TYPE_BOOLEAN1_BE: break;
		case TYPE_BOOLEAN4_BE: break;
		case TYPE_BOOLEAN8_BE: break;
		case TYPE_BOOLEAN16_BE: break;
		case TYPE_BOOLEAN24_BE: break;
		case TYPE_BOOLEAN32_BE: break;
		case TYPE_BOOLEAN48_BE: break;
		case TYPE_BOOLEAN64_BE: break;
		case TYPE_FIXED4_BE: break;
		case TYPE_FIXED8_BE: break;
		case TYPE_FIXED16_BE: break;
		case TYPE_FIXED24_BE: break;
		case TYPE_FIXED32_BE: break;
		case TYPE_FIXED48_BE: break;
		case TYPE_FIXED64_BE: break;
		case TYPE_FLOAT_QUARTER_BE: break;
		case TYPE_FLOAT_HALF_BE: break;
		case TYPE_FLOAT_3QUARTER_BE: break;
		case TYPE_FLOAT_SINGLE_BE: break;
		case TYPE_FLOAT_DOUBLE_BE: break;
		case TYPE_ENUM_BIT_BE: break;
		case TYPE_ENUM_NIBBLE_BE: break;
		case TYPE_ENUM_BYTE_BE: break;
		case TYPE_ENUM_WORD_BE: break;
		case TYPE_ENUM_LONG_BE: break;
		case TYPE_ENUM_LONG_LONG_BE: break;
		case TYPE_ENUM1_BE: break;
		case TYPE_ENUM4_BE: break;
		case TYPE_ENUM8_BE: break;
		case TYPE_ENUM16_BE: break;
		case TYPE_ENUM24_BE: break;
		case TYPE_ENUM32_BE: break;
		case TYPE_ENUM48_BE: break;
		case TYPE_ENUM64_BE: break;
		case TYPE_ENUM_OPTION_BE: break;
		case TYPE_SYMBOL_CHAR_BE: break;
		case TYPE_SYMBOL_TYPENAME_BE: break;
		case TYPE_SYMBOL_SYMBOL_BE: break;
		case TYPE_SYMBOL8_BE: break;
		case TYPE_SYMBOL16_BE: break;
		case TYPE_SYMBOL24_BE: break;
		case TYPE_SYMBOL32_BE: break;
		case TYPE_SYMBOL48_BE: break;
		case TYPE_SYMBOL64_BE: break;
		case TYPE_STRING_PASCAL8_BE: break;
		case TYPE_STRING_PASCAL16_BE: break;
		case TYPE_STRING_PASCAL24_BE: break;
		case TYPE_STRING_PASCAL32_BE: break;
		case TYPE_STRING_PASCAL48_BE: break;
		case TYPE_STRING_PASCAL64_BE: break;
		case TYPE_STRING_PASCAL8_EVEN_BE: break;
		case TYPE_STRING_PASCAL8_ODD_BE: break;
		case TYPE_STRING_PASCAL8_FIXED_BE: break;
		case TYPE_STRING_C_BE: break;
		case TYPE_STRING_C_EVEN_BE: break;
		case TYPE_STRING_C_ODD_BE: break;
		case TYPE_STRING_C_FIXED_BE: break;
		case TYPE_HEX_DUMP_BE: break;
		case TYPE_HEX_FIXED_BE: break;
		case TYPE_COORDINATE_2D_BE: break;
		case TYPE_COORDINATE_3D_BE: break;
		case TYPE_RECTANGLE_BE: break;
		case TYPE_COLOR_RGB555_BE: break;
		case TYPE_COLOR_RGB565_BE: break;
		case TYPE_COLOR_RGB444_BE: break;
		case TYPE_COLOR_RGB888_BE: break;
		case TYPE_COLOR_RGB161616_BE: break;
		case TYPE_COLOR_BGR555_BE: break;
		case TYPE_COLOR_BGR565_BE: break;
		case TYPE_COLOR_BGR444_BE: break;
		case TYPE_COLOR_BGR888_BE: break;
		case TYPE_COLOR_BGR161616_BE: break;
		case TYPE_LISTCOUNTER_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER4_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER8_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER16_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER24_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER32_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER48_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER64_ZEROBASED_BE: break;
		case TYPE_LISTCOUNTER_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER4_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER8_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER16_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER24_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER32_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER48_ONEBASED_BE: break;
		case TYPE_LISTCOUNTER64_ONEBASED_BE: break;
		case TYPE_LIST_EOF_TERMINATED_BE: break;
		case TYPE_LIST_ZERO_TERMINATED_BE: break;
		case TYPE_LIST_COUNTED_BE: break;
		case TYPE_LIST_END_BE: break;
		case TYPE_HEADER_BE: break;
		case TYPE_LABEL_BE: break;
		case TYPE_SEPARATOR_BE: break;
		case TYPE_COMMENT_BE: break;
		case TYPE_META_BE: break;
		case TYPE_IF_BE: break;
		case TYPE_ELSE_IF_BE: break;
		case TYPE_ELSE_BE: break;
		case TYPE_END_IF_BE: break;
		case TYPE_TEXT_ENCODING_BE: break;
		case TYPE_DEC_NIBBLE_LE: break;
		case TYPE_DEC_BYTE_LE: break;
		case TYPE_DEC_WORD_LE: break;
		case TYPE_DEC_LONG_LE: break;
		case TYPE_DEC_LONG_LONG_LE: break;
		case TYPE_DEC_SINT4_LE: break;
		case TYPE_DEC_SINT8_LE: break;
		case TYPE_DEC_SINT16_LE: break;
		case TYPE_DEC_SINT24_LE: break;
		case TYPE_DEC_SINT32_LE: break;
		case TYPE_DEC_SINT48_LE: break;
		case TYPE_DEC_SINT64_LE: break;
		case TYPE_DEC_UINT4_LE: break;
		case TYPE_DEC_UINT8_LE: break;
		case TYPE_DEC_UINT16_LE: break;
		case TYPE_DEC_UINT24_LE: break;
		case TYPE_DEC_UINT32_LE: break;
		case TYPE_DEC_UINT48_LE: break;
		case TYPE_DEC_UINT64_LE: break;
		case TYPE_HEX_NIBBLE_LE: break;
		case TYPE_HEX_BYTE_LE: break;
		case TYPE_HEX_WORD_LE: break;
		case TYPE_HEX_LONG_LE: break;
		case TYPE_HEX_LONG_LONG_LE: break;
		case TYPE_HEX_SINT4_LE: break;
		case TYPE_HEX_SINT8_LE: break;
		case TYPE_HEX_SINT16_LE: break;
		case TYPE_HEX_SINT24_LE: break;
		case TYPE_HEX_SINT32_LE: break;
		case TYPE_HEX_SINT48_LE: break;
		case TYPE_HEX_SINT64_LE: break;
		case TYPE_HEX_UINT4_LE: break;
		case TYPE_HEX_UINT8_LE: break;
		case TYPE_HEX_UINT16_LE: break;
		case TYPE_HEX_UINT24_LE: break;
		case TYPE_HEX_UINT32_LE: break;
		case TYPE_HEX_UINT48_LE: break;
		case TYPE_HEX_UINT64_LE: break;
		case TYPE_OCT_NIBBLE_LE: break;
		case TYPE_OCT_BYTE_LE: break;
		case TYPE_OCT_WORD_LE: break;
		case TYPE_OCT_LONG_LE: break;
		case TYPE_OCT_LONG_LONG_LE: break;
		case TYPE_OCT_SINT4_LE: break;
		case TYPE_OCT_SINT8_LE: break;
		case TYPE_OCT_SINT16_LE: break;
		case TYPE_OCT_SINT24_LE: break;
		case TYPE_OCT_SINT32_LE: break;
		case TYPE_OCT_SINT48_LE: break;
		case TYPE_OCT_SINT64_LE: break;
		case TYPE_OCT_UINT4_LE: break;
		case TYPE_OCT_UINT8_LE: break;
		case TYPE_OCT_UINT16_LE: break;
		case TYPE_OCT_UINT24_LE: break;
		case TYPE_OCT_UINT32_LE: break;
		case TYPE_OCT_UINT48_LE: break;
		case TYPE_OCT_UINT64_LE: break;
		case TYPE_BIN_NIBBLE_LE: break;
		case TYPE_BIN_BYTE_LE: break;
		case TYPE_BIN_WORD_LE: break;
		case TYPE_BIN_LONG_LE: break;
		case TYPE_BIN_LONG_LONG_LE: break;
		case TYPE_BIN_SINT4_LE: break;
		case TYPE_BIN_SINT8_LE: break;
		case TYPE_BIN_SINT16_LE: break;
		case TYPE_BIN_SINT24_LE: break;
		case TYPE_BIN_SINT32_LE: break;
		case TYPE_BIN_SINT48_LE: break;
		case TYPE_BIN_SINT64_LE: break;
		case TYPE_BIN_UINT4_LE: break;
		case TYPE_BIN_UINT8_LE: break;
		case TYPE_BIN_UINT16_LE: break;
		case TYPE_BIN_UINT24_LE: break;
		case TYPE_BIN_UINT32_LE: break;
		case TYPE_BIN_UINT48_LE: break;
		case TYPE_BIN_UINT64_LE: break;
		case TYPE_ALIGN_NIBBLE_LE: break;
		case TYPE_ALIGN_BYTE_LE: break;
		case TYPE_ALIGN_WORD_LE: break;
		case TYPE_ALIGN_LONG_LE: break;
		case TYPE_ALIGN_LONG_LONG_LE: break;
		case TYPE_ALIGN4_LE: break;
		case TYPE_ALIGN8_LE: break;
		case TYPE_ALIGN16_LE: break;
		case TYPE_ALIGN24_LE: break;
		case TYPE_ALIGN32_LE: break;
		case TYPE_ALIGN48_LE: break;
		case TYPE_ALIGN64_LE: break;
		case TYPE_FILLER_BIT_LE: break;
		case TYPE_FILLER_NIBBLE_LE: break;
		case TYPE_FILLER_BYTE_LE: break;
		case TYPE_FILLER_WORD_LE: break;
		case TYPE_FILLER_LONG_LE: break;
		case TYPE_FILLER_LONG_LONG_LE: break;
		case TYPE_FILLER1_LE: break;
		case TYPE_FILLER4_LE: break;
		case TYPE_FILLER8_LE: break;
		case TYPE_FILLER16_LE: break;
		case TYPE_FILLER24_LE: break;
		case TYPE_FILLER32_LE: break;
		case TYPE_FILLER48_LE: break;
		case TYPE_FILLER64_LE: break;
		case TYPE_BOOLEAN_BIT_LE: break;
		case TYPE_BOOLEAN_WORD_LE: break;
		case TYPE_BOOLEAN1_LE: break;
		case TYPE_BOOLEAN4_LE: break;
		case TYPE_BOOLEAN8_LE: break;
		case TYPE_BOOLEAN16_LE: break;
		case TYPE_BOOLEAN24_LE: break;
		case TYPE_BOOLEAN32_LE: break;
		case TYPE_BOOLEAN48_LE: break;
		case TYPE_BOOLEAN64_LE: break;
		case TYPE_FIXED4_LE: break;
		case TYPE_FIXED8_LE: break;
		case TYPE_FIXED16_LE: break;
		case TYPE_FIXED24_LE: break;
		case TYPE_FIXED32_LE: break;
		case TYPE_FIXED48_LE: break;
		case TYPE_FIXED64_LE: break;
		case TYPE_FLOAT_QUARTER_LE: break;
		case TYPE_FLOAT_HALF_LE: break;
		case TYPE_FLOAT_3QUARTER_LE: break;
		case TYPE_FLOAT_SINGLE_LE: break;
		case TYPE_FLOAT_DOUBLE_LE: break;
		case TYPE_ENUM_BIT_LE: break;
		case TYPE_ENUM_NIBBLE_LE: break;
		case TYPE_ENUM_BYTE_LE: break;
		case TYPE_ENUM_WORD_LE: break;
		case TYPE_ENUM_LONG_LE: break;
		case TYPE_ENUM_LONG_LONG_LE: break;
		case TYPE_ENUM1_LE: break;
		case TYPE_ENUM4_LE: break;
		case TYPE_ENUM8_LE: break;
		case TYPE_ENUM16_LE: break;
		case TYPE_ENUM24_LE: break;
		case TYPE_ENUM32_LE: break;
		case TYPE_ENUM48_LE: break;
		case TYPE_ENUM64_LE: break;
		case TYPE_ENUM_OPTION_LE: break;
		case TYPE_SYMBOL_CHAR_LE: break;
		case TYPE_SYMBOL_TYPENAME_LE: break;
		case TYPE_SYMBOL_SYMBOL_LE: break;
		case TYPE_SYMBOL8_LE: break;
		case TYPE_SYMBOL16_LE: break;
		case TYPE_SYMBOL24_LE: break;
		case TYPE_SYMBOL32_LE: break;
		case TYPE_SYMBOL48_LE: break;
		case TYPE_SYMBOL64_LE: break;
		case TYPE_STRING_PASCAL8_LE: break;
		case TYPE_STRING_PASCAL16_LE: break;
		case TYPE_STRING_PASCAL24_LE: break;
		case TYPE_STRING_PASCAL32_LE: break;
		case TYPE_STRING_PASCAL48_LE: break;
		case TYPE_STRING_PASCAL64_LE: break;
		case TYPE_STRING_PASCAL8_EVEN_LE: break;
		case TYPE_STRING_PASCAL8_ODD_LE: break;
		case TYPE_STRING_PASCAL8_FIXED_LE: break;
		case TYPE_STRING_C_LE: break;
		case TYPE_STRING_C_EVEN_LE: break;
		case TYPE_STRING_C_ODD_LE: break;
		case TYPE_STRING_C_FIXED_LE: break;
		case TYPE_HEX_DUMP_LE: break;
		case TYPE_HEX_FIXED_LE: break;
		case TYPE_COORDINATE_2D_LE: break;
		case TYPE_COORDINATE_3D_LE: break;
		case TYPE_RECTANGLE_LE: break;
		case TYPE_COLOR_RGB555_LE: break;
		case TYPE_COLOR_RGB565_LE: break;
		case TYPE_COLOR_RGB444_LE: break;
		case TYPE_COLOR_RGB888_LE: break;
		case TYPE_COLOR_RGB161616_LE: break;
		case TYPE_COLOR_BGR555_LE: break;
		case TYPE_COLOR_BGR565_LE: break;
		case TYPE_COLOR_BGR444_LE: break;
		case TYPE_COLOR_BGR888_LE: break;
		case TYPE_COLOR_BGR161616_LE: break;
		case TYPE_LISTCOUNTER_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER4_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER8_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER16_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER24_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER32_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER48_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER64_ZEROBASED_LE: break;
		case TYPE_LISTCOUNTER_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER4_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER8_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER16_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER24_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER32_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER48_ONEBASED_LE: break;
		case TYPE_LISTCOUNTER64_ONEBASED_LE: break;
		case TYPE_LIST_EOF_TERMINATED_LE: break;
		case TYPE_LIST_ZERO_TERMINATED_LE: break;
		case TYPE_LIST_COUNTED_LE: break;
		case TYPE_LIST_END_LE: break;
		case TYPE_HEADER_LE: break;
		case TYPE_LABEL_LE: break;
		case TYPE_SEPARATOR_LE: break;
		case TYPE_COMMENT_LE: break;
		case TYPE_META_LE: break;
		case TYPE_IF_LE: break;
		case TYPE_ELSE_IF_LE: break;
		case TYPE_ELSE_LE: break;
		case TYPE_END_IF_LE: break;
		case TYPE_TEXT_ENCODING_LE: break;
		}
	}
}
