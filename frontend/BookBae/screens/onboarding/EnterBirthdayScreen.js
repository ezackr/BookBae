import React, { useState } from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Button, Pressable } from 'react-native';
import DatePicker from 'react-native-date-picker';
import moment from 'moment';
import { AgeFromDate } from 'age-calculator';

const EnterBirthdayScreen = ({route, navigation}) => {

    const [date, setDate] = useState(new Date());
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState('');

    //add necessary function to store birthday
    const onPress = () => {
        const age = new AgeFromDate(date).age;
        if (age < 18) {
            setMessage('You must be at least 18 years old to use this app.')
        } else {
            console.log(date)
            navigation.navigate('EnterZipcodeScreen', {
                ...route.params,
                birthday: moment(date).format('YYYY-MM-DD')
            })
        }
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Birthday</Text>
            <Button title="Enter Date" onPress={() => setOpen(true)} />
            <DatePicker
                modal
                open={open}
                mode="date"
                date={date}
                onConfirm={(date) => {
                  setOpen(false)
                  setDate(date)
                }}
                onCancel={() => {
                  setOpen(false)
                }}
              />
              <Text> {message} </Text>
            <Pressable
                style={styles.button}
                onPress={onPress}>
                <Text style={styles.buttonText}>Next</Text>
             </Pressable>
        </SafeAreaView>
    );

}

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  button: {
      alignItems: 'center',
      justifyContent: 'center',
      paddingVertical: 12,
      paddingHorizontal: 32,
      borderRadius: 4,
      elevation: 3,
      backgroundColor: '#BD2A2A',
      marginTop: 5,
      marginBottom: 5,
  },
  buttonText: {
      fontSize: 16,
      lineHeight: 21,
      fontWeight: 'bold',
      letterSpacing: 0.25,
      color: 'white',
  },
});

export default EnterBirthdayScreen;