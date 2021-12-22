package utils.collector.storagesystem;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

public class Double2Point extends JsonSerializer<Double> {

	@Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		
		if (value == null) {
			value = 0.00;
			gen.writeString(value + "");
		} else {
			DecimalFormat df = new DecimalFormat("#.00");
			gen.writeString(df.format(value));
		}
		
	}

}
