using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class Checkpoint
    {
        public int ID { get; set; }
        public string Name { get; set; }
        public string Scan { get; set; }
        public string Hint { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public int Points { get; set; }

        public Checkpoint(int id, string name, string scan, string hint, double latitude, double longitude, int points)
        {
            ID = id;
            Name = name;
            Scan = scan;
            Hint = hint;
            Latitude = latitude;
            Longitude = longitude;
            Points = points;
        }

        public Checkpoint()
        {
        }
    }
}
