
import DateTimePicker, { DateTimePickerEvent } from '@react-native-community/datetimepicker';
import React from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Platform,
} from 'react-native';

export interface IProps {
  date?: Date;
  placeholder?: string;
  onDateChange?: (date: Date) => void;
}

export interface IState {
  date: Date;
  show: boolean;
}

class CustomDatePicker extends React.Component<IProps, IState> {
  state: IState = {
    date: this.props.date ?? new Date(),
    show: false,
  };

  componentDidUpdate(prevProps: IProps) {
    if (prevProps.date !== this.props.date && this.props.date) {
      this.setState({ date: this.props.date });
    }
  }

  onChange = (event: DateTimePickerEvent, selectedDate?: Date) => {
    if (event.type === 'set' && selectedDate) {
      this.setState({ date: selectedDate });
      this.props.onDateChange?.(selectedDate);
    }

    if (event.type === 'dismissed') {
      this.setState({ show: false });
    }
  };
  render() {
    const { date, show } = this.state;
    const { placeholder = 'Select Date of Birth' } = this.props;

    return (
      <View style={styles.container}>
        <TouchableOpacity
          style={styles.inputContainer}
          onPress={() => this.setState({ show: !show })}
        >
          <Text style={styles.text}>
            {this.props.date || date
              ? date.toDateString()
              : placeholder}
          </Text>
        </TouchableOpacity>

        {show && (
          <View style={styles.pickerContainer}>
            <DateTimePicker
              value={date}
              mode="date"
              display="spinner"
              onChange={this.onChange}
              textColor="#000000"
              accentColor="#00C2A8"
              themeVariant="dark"
            />
          </View>
        )}
      </View>
    );
  }
}

export default CustomDatePicker;

const styles = StyleSheet.create({
  container: {
    width: '100%',
  },
  inputContainer: {
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: '#CAD3DF',
    borderRadius: 15,
    marginHorizontal: 30,
    marginVertical: 12,
    paddingHorizontal: 15,
    height: 50,
    backgroundColor: '#131314',
  },
  pickerContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 10,
  },
  text: {
    fontFamily: 'Poppins',
    color: '#fff',
    fontSize: 16,
  },
});
