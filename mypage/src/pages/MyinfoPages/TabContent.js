import React from 'react';

import RecipeCard from './RecipeCard';
import Myinfo from './Myinfo';

const TabContent = ({tab}) => {

  if(tab === 0) {
    return <Myinfo />
  }else if(tab === 1) {
    return;
  }else {
    return <RecipeCard />
  }
}

export default TabContent