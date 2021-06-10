import React from "react";
import { Text, StyleSheet } from "react-native";

// This is a JSX.Element
const HomeScreen = () => {
  return (
    <>
      <Text style={styles.title}>Muster</Text>
      <Text style={styles.text}>An app to support Jesus' Ministries</Text>
    </>
  )
};

const styles = StyleSheet.create({
  title: {
    fontSize: 30
  },
  text: {
    fontSize: 14
  }
});

export default HomeScreen;
