using LogicUniversityApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LogicUniversityApp
{
    public class DbFactory
    {
        private static DbFactory instance = null;

        private DbFactory()
        {
        }

        public LogicUniversityDbContext context = new LogicUniversityDbContext();

        public static DbFactory Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new DbFactory();
                }
                return instance;
            }
        }
    }
}