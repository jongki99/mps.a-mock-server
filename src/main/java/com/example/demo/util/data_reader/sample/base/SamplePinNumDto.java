package com.example.demo.util.data_reader.sample.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SamplePinNumDto {
	String pinNum;
	String staDtm;
	String endDtm;
	String pinUrl;
}
