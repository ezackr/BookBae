import React, {useState} from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Pressable } from 'react-native';
import { Picker } from '@react-native-picker/picker'

const EnterGenreScreen = ({route, navigation}) => {

    const [selectedValue, setSelectedValue] = useState("java");

    //add necessary function to store favorite genre
    const onPress = () => {
        console.log(selectedValue)
        console.log(route.params.zipcode)
        navigation.navigate('EnterBooksScreen', {
            ...route.params,
            favGenre: selectedValue
        })
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Favorite Genre</Text>
            <Picker
                selectedValue={selectedValue}
                style={{ height: 50, width: 150 }}
                onValueChange={(itemValue, itemIndex) => setSelectedValue(itemValue)}>
                <Picker.Item label="Fantasy" value="fantasy" />
                <Picker.Item label="Mystery" value="mystery" />
                <Picker.Item label="Science Fiction" value="scifi" />
                <Picker.Item label="Dystopian" value="dystopian" />
                <Picker.Item label="Romance" value="romance" />
                <Picker.Item label="Biography" value="biography" />
                <Picker.Item label="History" value="history" />
                <Picker.Item label="True Crime" value="trueCrime" />
              </Picker>
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

export default EnterGenreScreen;